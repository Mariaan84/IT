package entity;

import entity.column.Column;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface DatabaseManager extends Remote {
    List<Row> tablesIntersection(String firstTableName,
                                 String secondTableName,
                                 String firstTableColumnName,
                                 String secondColumnTableName) throws RemoteException;

    Database createDatabase(final String name) throws RemoteException;

    void addTable(String name) throws RemoteException;

    Table getTable(String name) throws RemoteException;

    void addColumn(String tableName, Column column) throws RemoteException;

    void addRow(String tableName, Row row) throws RemoteException;
}
