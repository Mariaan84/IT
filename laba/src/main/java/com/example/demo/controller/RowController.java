package com.example.demo.controller;

import entity.DatabaseManagerImpl;
import entity.Row;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.rmi.RemoteException;
import java.util.List;

@RestController
@RequestMapping("/row")
public class RowController {
    private static DatabaseManagerImpl databaseManager;

    static {
        try {
            databaseManager = DatabaseManagerImpl.getInstance();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @GetMapping("{tableName}")
    public ResponseEntity<List<Row>> getRows(@PathVariable String tableName) {
        return ResponseEntity.ok(databaseManager.getTable(tableName).getRows());
    }

    @PostMapping
    public ResponseEntity<Row> addRow(@RequestParam("tableName") String tableName,
                                            @RequestBody List<String> rowValues) {
        Row row = new Row(rowValues);
        databaseManager.addRow(tableName, row);
        return ResponseEntity.ok(row);
    }
}
