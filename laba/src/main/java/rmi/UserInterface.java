package rmi;

import entity.DatabaseManager;
import entity.DatabaseManagerImpl;
import entity.Row;
import entity.Table;
import entity.column.*;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.swing.*;
import java.awt.*;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class UserInterface extends JFrame {
    private static final List<Table> tables = new ArrayList<>();
    public static JTextField dbname;
    public static JTextArea resArea;
    public static DatabaseManager databaseManager;

    public static void main(String[] args) throws NamingException, RemoteException {
        Context context = new InitialContext();
        DatabaseManager database = (DatabaseManager) context.lookup("rmi://localhost/database");

        JFrame frame = new JFrame();
        frame.setTitle("My Database");
        frame.setBounds(300, 90, 900, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);

        JLabel title = new JLabel("My Database");
        title.setFont(new Font("Arial", Font.PLAIN, 20));
        title.setSize(300, 20);
        title.setLocation(400, 30);
        title.setVisible(true);
        frame.add(title);

        JLabel name = new JLabel("Database name");
        name.setFont(new Font("Arial", Font.PLAIN, 20));
        name.setSize(200, 20);
        name.setLocation(100, 100);
        frame.add(name);

        dbname = new JTextField();
        dbname.setFont(new Font("Arial", Font.PLAIN, 15));
        dbname.setSize(150, 20);
        dbname.setLocation(250, 100);
        frame.add(dbname);
        UserInterface.databaseManager = DatabaseManagerImpl.getInstance();

        JButton createDbBtn = new JButton("Create Database");
        createDbBtn.setFont(new Font("Arial", Font.PLAIN, 15));
        createDbBtn.setSize(300, 20);
        createDbBtn.setLocation(100, 150);
        frame.add(createDbBtn);
        createDbBtn.addActionListener(e -> {
            try {
                databaseManager.createDatabase(UserInterface.dbname.getText());
            } catch (RemoteException ex) {
                ex.printStackTrace();
            }
        });
        JLabel tableName = new JLabel("Table name");
        tableName.setVisible(true);
        tableName.setFont(new Font("Arial", Font.PLAIN, 20));
        tableName.setSize(300, 20);
        tableName.setLocation(100, 200);
        frame.add(tableName);

        JTextField tableNameField = new JTextField();
        tableNameField.setVisible(true);
        tableNameField.setFont(new Font("Arial", Font.PLAIN, 15));
        tableNameField.setSize(190, 20);
        tableNameField.setLocation(210, 200);
        frame.add(tableNameField);

        JButton createTableBtn = new JButton("Create Table");
        createTableBtn.setFont(new Font("Arial", Font.PLAIN, 15));
        createTableBtn.setSize(300, 20);
        createTableBtn.setLocation(100, 250);
        frame.add(createTableBtn);
        createTableBtn.addActionListener(e -> {
            try {
                databaseManager.addTable(tableNameField.getText());
            } catch (RemoteException ex) {
                ex.printStackTrace();
            }
            try {
                tables.add(databaseManager.getTable(tableNameField.getText()));
            } catch (RemoteException ex) {
                ex.printStackTrace();
            }
            for (Table t : tables)
                resArea.setText(t.getName() + "\n");
            JFrame tableFrame = new JFrame();// створити фрейм
            tableFrame.setTitle("Table creation");
            tableFrame.setBounds(300, 90, 400, 400);
            tableFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
            tableFrame.setLayout(null);

            JButton addColBtn = new JButton("Add Column");
            addColBtn.setFont(new Font("Arial", Font.PLAIN, 15));
            addColBtn.setSize(150, 20);
            addColBtn.setLocation(120, 100);
            tableFrame.add(addColBtn);
            addColBtn.addActionListener(e1 -> addColumn(tableNameField.getText()));
            JButton addRowBtn = new JButton("Add Row");
            addRowBtn.setFont(new Font("Arial", Font.PLAIN, 15));
            addRowBtn.setSize(150, 20);
            addRowBtn.setLocation(120, 200);
            tableFrame.add(addRowBtn);
            addRowBtn.addActionListener(e12 -> {
                try {
                    addRow(tableNameField.getText());
                } catch (RemoteException ex) {
                    ex.printStackTrace();
                }
            });
            tableFrame.setVisible(true);
        });

        //find tables difference
        JLabel difTitle = new JLabel("Find tables intersection");
        difTitle.setVisible(true);
        difTitle.setFont(new Font("Arial", Font.PLAIN, 20));
        difTitle.setSize(350, 20);
        difTitle.setLocation(150, 300);
        frame.add(difTitle);

        JLabel difTableName1 = new JLabel("Table 1");
        difTableName1.setVisible(true);
        difTableName1.setFont(new Font("Arial", Font.PLAIN, 20));
        difTableName1.setSize(80, 20);
        difTableName1.setLocation(100, 350);
        frame.add(difTableName1);

        JTextField difTableNameField1 = new JTextField();
        difTableNameField1.setVisible(true);
        difTableNameField1.setFont(new Font("Arial", Font.PLAIN, 15));
        difTableNameField1.setSize(50, 20);
        difTableNameField1.setLocation(180, 350);
        frame.add(difTableNameField1);

        JLabel firstTableColumnName = new JLabel("Column 1");
        firstTableColumnName.setVisible(true);
        firstTableColumnName.setFont(new Font("Arial", Font.PLAIN, 20));
        firstTableColumnName.setSize(100, 20);
        firstTableColumnName.setLocation(250, 350);
        frame.add(firstTableColumnName);

        JTextField firstTableColumn = new JTextField();
        firstTableColumn.setVisible(true);
        firstTableColumn.setFont(new Font("Arial", Font.PLAIN, 15));
        firstTableColumn.setSize(50, 20);
        firstTableColumn.setLocation(350, 350);
        frame.add(firstTableColumn);

        JLabel difTableName2 = new JLabel("Table 2");
        difTableName2.setVisible(true);
        difTableName2.setFont(new Font("Arial", Font.PLAIN, 20));
        difTableName2.setSize(80, 20);
        difTableName2.setLocation(100, 400);
        frame.add(difTableName2);

        JTextField difTableNameField2 = new JTextField();
        difTableNameField2.setVisible(true);
        difTableNameField2.setFont(new Font("Arial", Font.PLAIN, 15));
        difTableNameField2.setSize(50, 20);
        difTableNameField2.setLocation(180, 400);
        frame.add(difTableNameField2);

        JLabel secondTableColumnName = new JLabel("Column 2");
        secondTableColumnName.setVisible(true);
        secondTableColumnName.setFont(new Font("Arial", Font.PLAIN, 20));
        secondTableColumnName.setSize(100, 20);
        secondTableColumnName.setLocation(250, 400);
        frame.add(secondTableColumnName);

        JTextField secondTableColumn = new JTextField();
        secondTableColumn.setVisible(true);
        secondTableColumn.setFont(new Font("Arial", Font.PLAIN, 15));
        secondTableColumn.setSize(50, 20);
        secondTableColumn.setLocation(350, 400);
        System.out.println(database);
        frame.add(secondTableColumn);

        JButton findDifBtn = new JButton("Find intersection");
        findDifBtn.setFont(new Font("Arial", Font.PLAIN, 15));
        findDifBtn.setSize(300, 20);
        findDifBtn.setLocation(100, 450);
        frame.add(findDifBtn);
        findDifBtn.addActionListener(e -> {
            Table table1 = null;
            Table table2 = null;
            for (Table table : tables) {
                if (table.getName().equalsIgnoreCase(difTableNameField1.getText())) {
                    table1 = table;
                }
                if (table.getName().equalsIgnoreCase(difTableNameField2.getText())) {
                    table2 = table;
                }
            }
            assert table1 != null;
            assert table2 != null;
            List<Row> rows = null;
            try {
                rows = databaseManager.tablesIntersection(table1.getName(),
                    table2.getName(),
                    firstTableColumn.getText(),
                    secondTableColumn.getText());
            } catch (RemoteException ex) {
                ex.printStackTrace();
            }
            resArea.setText("Intersection result: " + rows);
        });

        JButton saveDbBtn = new JButton("Save database");
        saveDbBtn.setFont(new Font("Arial", Font.PLAIN, 15));
        saveDbBtn.setSize(150, 20);
        saveDbBtn.setLocation(100, 500);
        frame.add(saveDbBtn);

        JButton showDbBtn = new JButton("Show database");
        showDbBtn.setFont(new Font("Arial", Font.PLAIN, 15));
        showDbBtn.setSize(150, 20);
        showDbBtn.setLocation(250, 500);
        showDbBtn.addActionListener(e -> {
            StringBuilder tableBuilder = new StringBuilder();
            tables.forEach(table -> tableBuilder.append(showTable(table)).append("\n"));
            resArea.setText(tableBuilder.toString());
        });
        frame.add(showDbBtn);

        resArea = new JTextArea();
        resArea.setFont(new Font("Arial", Font.PLAIN, 15));
        resArea.setSize(300, 400);
        resArea.setLocation(500, 100);
        resArea.setLineWrap(true);
        resArea.setEditable(false);
        frame.add(resArea);

        frame.setVisible(true);
    }

    public static void addColumn(String curTableName) {
        for (Table table : tables) {
            if (table.getName().equals(curTableName)) {
                JFrame f = new JFrame();
                f.setTitle("Adding columns");
                f.setBounds(300, 90, 500, 400);
                f.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
                f.setLayout(null);

                JLabel columnName = new JLabel("Column name");
                JTextField colNameField = new JTextField();
                setUp(f, columnName, colNameField);
                colNameField.setLocation(260, 100);
                f.add(colNameField);

                JLabel colType = new JLabel("Column type");
                colType.setFont(new Font("Arial", Font.PLAIN, 18));
                colType.setSize(200, 20);
                colType.setLocation(100, 200);
                f.add(colType);

                String[] dataTypes = {"Integer", "Real", "Char", "String", "$Invl"};
                JComboBox<String> colTypeBox = new JComboBox<>(dataTypes);
                colTypeBox.setFont(new Font("Arial", Font.PLAIN, 15));
                colTypeBox.setSize(100, 20);
                colTypeBox.setLocation(260, 200);
                f.add(colTypeBox);

                JButton addColumn = new JButton("Add column");
                addColumn.setFont(new Font("Arial", Font.PLAIN, 15));
                addColumn.setSize(200, 20);
                addColumn.setLocation(150, 250);
                f.add(addColumn);
                f.setVisible(true);
                addColumn.addActionListener(e -> {
                    if (Objects.equals(colTypeBox.getSelectedItem(), "Integer")) {
                        try {
                            databaseManager.addColumn(table.getName(),
                                new IntColumn(colNameField.getText()));
                        } catch (RemoteException ex) {
                            ex.printStackTrace();
                        }
                    }
                    if (Objects.equals(colTypeBox.getSelectedItem(), "Real")) {
                        try {
                            databaseManager.addColumn(table.getName(),
                                new RealColumn(colNameField.getText()));
                        } catch (RemoteException ex) {
                            ex.printStackTrace();
                        }
                    }
                    if (Objects.equals(colTypeBox.getSelectedItem(), "Char")) {
                        try {
                            databaseManager.addColumn(table.getName(),
                                new CharColumn(colNameField.getText()));
                        } catch (RemoteException ex) {
                            ex.printStackTrace();
                        }
                    }
                    if (Objects.equals(colTypeBox.getSelectedItem(), "String")) {
                        try {
                            databaseManager.addColumn(table.getName(),
                                new StringColumn(colNameField.getText()));
                        } catch (RemoteException ex) {
                            ex.printStackTrace();
                        }
                    }
                    if (Objects.equals(colTypeBox.getSelectedItem(), "$Invl")) {
                        try {
                            databaseManager.addColumn(table.getName(),
                                new IntervalColumn(colNameField.getText()));
                        } catch (RemoteException ex) {
                            ex.printStackTrace();
                        }
                    }
                    String def = "";
                    colNameField.setText(def);
                });
            }
        }
    }

    private static void setUp(JFrame f, JLabel columnName, JTextField colNameField) {
        columnName.setFont(new Font("Arial", Font.PLAIN, 18));
        columnName.setSize(200, 20);
        columnName.setLocation(100, 100);
        f.add(columnName);

        colNameField.setFont(new Font("Arial", Font.PLAIN, 15));
        colNameField.setSize(190, 20);
    }

    public static void addRow(String curTableName) throws RemoteException {
        for (Table table : tables) {
            if (table.getName().equals(curTableName)) {
                List<String> row = new ArrayList<>();
                for (int i = 0; i < table.getColumns().size(); i++) {
                    Column column = table.getColumns().get(table.getColumns().size() - i - 1);
                    switch (column.getType()) {
                        case INTEGER, REAL, CHAR, STRING -> setUp(row, column.getName());
                        case INTEGER_INTERVAL -> {
                            JFrame fr = new JFrame();
                            fr.setTitle("Adding rows");
                            fr.setBounds(300, 90, 500, 500);
                            fr.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
                            fr.setLayout(null);
                            JLabel rowValue = new JLabel("Min value");
                            JTextField rowValueField = new JTextField();
                            setUp(fr, rowValue, rowValueField);
                            rowValueField.setLocation(210, 100);
                            fr.add(rowValueField);
                            JLabel row2Value = new JLabel("Max value");
                            row2Value.setFont(new Font("Arial", Font.PLAIN, 18));
                            row2Value.setSize(200, 20);
                            row2Value.setLocation(100, 200);
                            fr.add(row2Value);
                            JTextField row2ValueField = new JTextField();
                            row2ValueField.setFont(new Font("Arial", Font.PLAIN, 15));
                            row2ValueField.setSize(190, 20);
                            row2ValueField.setLocation(210, 200);
                            fr.add(row2ValueField);
                            JButton setValue = new JButton("Set value");
                            setValue.setFont(new Font("Arial", Font.PLAIN, 15));
                            setValue.setSize(200, 20);
                            setValue.setLocation(150, 250);
                            fr.add(setValue);
                            setValue.addActionListener(e ->
                                row.add("[" + rowValueField.getText() + " : " + row2ValueField.getText() + "]"));
                            fr.setVisible(true);
                        }
                    }
                }
                databaseManager.addRow(table.getName(), new Row(row));
            }
        }
    }

    private static void setUp(List<String> row, String name) {
        JFrame fr = new JFrame();
        fr.setTitle("Adding rows");
        fr.setBounds(300, 90, 500, 400);
        fr.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        fr.setLayout(null);

        JLabel rowValue = new JLabel(name);
        rowValue.setFont(new Font("Arial", Font.PLAIN, 18));
        rowValue.setSize(230, 20);
        rowValue.setLocation(100, 100);
        fr.add(rowValue);

        JTextField rowValueField = new JTextField();
        rowValueField.setFont(new Font("Arial", Font.PLAIN, 15));
        rowValueField.setSize(190, 20);
        rowValueField.setLocation(250, 100);

        fr.add(rowValueField);

        JButton setValue = new JButton("Set value");
        setValue.setFont(new Font("Arial", Font.PLAIN, 15));
        setValue.setSize(200, 20);
        setValue.setLocation(150, 250);
        fr.add(setValue);

        fr.setVisible(true);
        setValue.addActionListener(e -> row.add(rowValueField.getText()));
    }

    private static String showTable(Table table) {
        StringBuilder tableBuilder = new StringBuilder();
        tableBuilder.append("Table: ")
            .append(table.getName()).append("\n")
            .append("Columns: ").append("\n");
        for (Column column : table.getColumns()) {
            tableBuilder
                .append(" - ")
                .append(column.getName()).append(" : ")
                .append(column.getType()).append("\n");
        }
        tableBuilder.append("Rows: ").append("\n")
            .append(table.getRows());
        return tableBuilder.toString();
    }
}

