package entity;

import entity.column.*;
import lombok.Getter;
import lombok.Setter;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
@Setter
public class DatabaseManagerImpl extends UnicastRemoteObject implements DatabaseManager {
    private static DatabaseManagerImpl instance;
    private static String DELIMITER = "$";
    private static String SPACE = "\t";
    private static String SPECIAL_CHARS = "\\/:*?\"<>|";
    private static String LINE_DELIMITER = "\\" + DELIMITER;

    private Database database;

    public DatabaseManagerImpl() throws RemoteException {
    }

    public static DatabaseManagerImpl getInstance() throws RemoteException {
        if (Objects.isNull(instance)) {
            instance = new DatabaseManagerImpl();
        }
        return instance;
    }


    public Database createDatabase(final String name) {
        database = new Database(name);
        return database;
    }

    public void SaveDatabase(String filePath) {
        try {
            PrintWriter printWriter = new PrintWriter(new FileWriter(filePath));
            printWriter.print(database.getName());
            printWriter.close();
        } catch (IOException exception) {
            throw new RuntimeException("an error occurred during saving the DB: "
                + database.getName()
                + "by path: "
                + filePath);
        }
    }

    public void openDatabase(String filePath) {
        try {
            String content = Files.readString(Path.of(filePath));
            String[] lines = content.split(LINE_DELIMITER);

            if (lines.length == 0) {
                throw new RuntimeException("database is empty by path: " + filePath);
            }
            database = new Database(lines[0]);

            readTablesFromFile(lines);
        } catch (IOException exception) {
            throw new RuntimeException("an error occurred during opening the DB by path: "
                + filePath);
        }
    }

    public void deleteDatabase(String filePath) {
        try {
            if (!filePath.isBlank()) {
                Files.delete(Path.of(filePath));
                database = null;
            }
        } catch (IOException exception) {
            throw new RuntimeException("an error occurred during deleting DB, path: " + filePath);
        }
    }

    public void addTable(String name) {
        if (getTableNames().contains(name)) {
            throw new RuntimeException("table with name: "
                + name
                + "already exists");
        }
            database.getTables().add(new Table(name));
    }

    public Table getTable(int index) {
        return database.getTables().get(index);
    }

    public Table getTable(String name) {
        return database.getTables().stream()
            .filter(table -> name.equals(table.getName()))
            .findAny()
            .orElse(null);
    }

    public Column getColumn(String tableName, String columnName) {
        List<Column> columnsFromTable = getColumns(tableName);
        return columnsFromTable.stream()
            .filter(column -> columnName.equals(column.getName())).toList().get(0);
    }

    public List<String> getTableNames() {
        if (Objects.isNull(database.getTables())) {
            return Collections.emptyList();
        }
        return database.getTables().stream()
            .map(Table::getName).collect(Collectors.toList());
    }

    public List<String> getColumnNames(String tableName) {
        if (Objects.isNull(getTable(tableName).getColumns())) {
            return new ArrayList<>();
        }
        return getTable(tableName)
            .getColumns().stream()
            .map(Column::getName).collect(Collectors.toList());
    }

    public List<Column> getColumns(String tableName) {
        return getTable(tableName).getColumns();
    }

    public void deleteTable(int index) {
        database.getTables().remove(index);
    }

    public void deleteTable(String name) {
        database.setTables(
            database.getTables().stream()
                .filter(table -> !table.getName().equals(name)).toList());
    }

    public void addColumn(String tableName, Column column) {
        if (getColumnNames(tableName).contains(column.getName())) {
            throw new RuntimeException("column with name: "
                + column.getName()
                + " already exists in the table: "
                + tableName);
        }
        Table table = getTable(tableName);
        table.getColumns().add(column);

        for (Row row : table.getRows()) {
            row.getValues().add("");
        }
    }

    //TODO write remove column by name method
    public void deleteColumn(String tableName, int columnIndex) {
        Table table = getTable(tableName);
        table.getColumns().remove(columnIndex);

        for (Row row : table.getRows()) {
            row.getValues().remove(columnIndex);
        }

        if (table.getColumns().size() == 0) {
            table.setRows(new ArrayList<>());
        }
    }

    public void addRow(String tableName, Row row) {
        if (columnsIsNull(tableName)) {
            throw new RuntimeException("there are no columns in a table: "
                + tableName);
        }

        Table table = getTable(tableName);
        table.getRows().add(row);
    }

    //TODO write delete by name method
    public void DeleteRow(int tableIndex, int rowIndex) {
        database.getTables().get(tableIndex).getRows().remove(rowIndex);
    }

    //TODO use names instead of indexes
    public boolean changeCellValue(String newValue,
                                   int tableIndex,
                                   int columnIndex,
                                   int rowIndex) {
        if (database.getTables().get(tableIndex)
            .getColumns().get(columnIndex).validate(newValue)) {
            database.getTables().get(tableIndex)
                .getRows().get(rowIndex).getValues().set(columnIndex, newValue);
            return true;
        }

        return false;
    }

    private boolean columnsIsNull(String tableName) {
        return Objects.isNull(database)
            || database.getTables().size() == 0
            || getTable(tableName).getColumns().size() == 0;
    }

    private void readTablesFromFile(String[] parts) {
        for (int i = 0; i < parts.length; i++) {
            if (parts[i].isEmpty()){
                continue;
            }

            parts[i] = parts[i].replaceAll("\r\n", "\r");
            List<String> buf = Arrays.stream(parts[i].split("\r")).collect(Collectors.toList());

            if(buf.size() == 0){
                return;
            }

            buf.remove(0);

            Table table = new Table(buf.get(0));

            if (buf.size() > 2) {
                readColumnsFromFile(buf, table);
                readRowsFromFile(buf, table);
                database.getTables().add(table);
            }
        }
    }

    private void readColumnsFromFile(List<String> buf, Table table) {
        String[] columnNames = buf.get(1).split(SPACE);
        String[] columnTypes = buf.get(2).split(SPACE);

        for (int j = 0; j < columnNames.length - 1; j++) {
            table.getColumns().add(
                columnFromString(columnNames[j], columnTypes[j])
            );
        }
    }

    private void readRowsFromFile(List<String> buf, Table table) {
        for (int j = 3; j < buf.size(); j++) {
            table.getRows().add(new Row(Arrays.stream(buf.get(j).split(SPACE))
                .collect(Collectors.toList())));
        }
    }

    public void writeTablesToFile(PrintWriter writer) {
        for (Table table : database.getTables()) {
            writer.println(DELIMITER);
            writer.println(table.getName());

            writeColumnsToFile(writer, table);
            writeRowsToFile(writer, table);
        }
    }

    private static void writeColumnsToFile(PrintWriter writer, Table table) {
        for (Column column : table.getColumns()) {
            writer.print(column.getName() + SPACE);
        }

        writer.println();

        for (Column column : table.getColumns()) {
            writer.print(column.getType() + SPACE);
        }

        writer.println();
    }

    private static void writeRowsToFile(PrintWriter writer, Table table) {
        for (Row row : table.getRows()) {
            for (String value : row.getValues()) {
                writer.write(value + SPACE);
            }
            writer.println();
        }
    }

    public static Column columnFromString(String name, String type) {
        return switch (type) {
            case "INT" -> new IntColumn(name);
            case "REAL" -> new RealColumn(name);
            case "CHAR" -> new CharColumn(name);
            case "STRING" -> new StringColumn(name);
            case "TEXT FILE" -> new TextFileColumn(name);
            case "INT INTERVAL" -> new IntervalColumn(name);
            default -> null;
        };
    }

    @Override
    public List<Row> tablesIntersection(String firstTableName,
        String secondTableName,
        String firstTableColumnName,
        String secondColumnTableName) {
        List<Row> rowsFromFistTable = getTable(firstTableName).getRows();
        List<Row> rowsFromSecondTable = getTable(secondTableName).getRows();

        Column firstColumn = getColumn(firstTableName, firstTableColumnName);
        Column secondColumn = getColumn(secondTableName, secondColumnTableName);

        Map<Row, Row> intersectedRowsMap = getIntersectedRowsMap(rowsFromFistTable,
            rowsFromSecondTable, firstTableName, secondTableName, firstColumn, secondColumn);

        return getListFromIntersectedRowNap(intersectedRowsMap);
    }

    private Map<Row, Row> getIntersectedRowsMap(List<Row> rowsFromFirstTable,
        List<Row> rowsFromSecondTable,
        String firstTableName,
        String secondTableName,
        Column firstColumn,
        Column secondColumn) {
        Map<Row, Row> intersectedRowsMap = new HashMap<>();
        for(int i = 0; i < rowsFromFirstTable.size(); i++) {
            if (rowsFromFirstTable.get(i).getValues()
                .get(getColumnIndex(firstTableName, firstColumn))
                .equals(rowsFromSecondTable.get(i).getValues()
                    .get(getColumnIndex(secondTableName,secondColumn)))) {
                intersectedRowsMap.put(rowsFromFirstTable.get(i), rowsFromSecondTable.get(i));
            }
        }
        return intersectedRowsMap;
    }

    private List<Row> getListFromIntersectedRowNap(Map<Row, Row> intersectedRowsMap) {
        List<Row> intersectedRows = new ArrayList<>();
        for (Entry<Row, Row> entry: intersectedRowsMap.entrySet()) {
            intersectedRows.add(new Row(
                Stream.concat(entry.getKey().getValues().stream(),
                        entry.getValue().getValues().stream())
                    .collect(Collectors.toList())));
        }
        return intersectedRows;
    }

    private int getColumnIndex(String tableName, Column column) {
        return getTable(tableName).getColumns().indexOf(column);
    }
}
