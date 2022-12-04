package com.example.demo.controller;

import entity.DatabaseManagerImpl;
import entity.column.Column;
import entity.column.IntColumn;
import entity.column.StringColumn;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.rmi.RemoteException;

@RestController
@RequestMapping("/column")
public class ColumnController {
    private static DatabaseManagerImpl databaseManager;

    static {
        try {
            databaseManager = DatabaseManagerImpl.getInstance();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @GetMapping("{tableName}/{columnName}")
    public ResponseEntity<Column> getColumn(@PathVariable String tableName,
                                            @PathVariable String columnName) {
        return ResponseEntity.ok(databaseManager.getColumn(tableName, columnName));
    }

    @PostMapping
    public ResponseEntity<Column> addColumn(@RequestParam("tableName") String tableName,
                                            @RequestParam("columnName") String columnName,
                                            @RequestParam("columnType") String columnType) {
        switch (columnType) {
            case "INTEGER" -> databaseManager.addColumn(tableName, new IntColumn(columnName));
            case "STRING" -> databaseManager.addColumn(tableName, new StringColumn(columnName));
        }
        return ResponseEntity.ok(databaseManager.getColumn(tableName, columnName));
    }
}
