package com.example.demo.controller;

import entity.DatabaseManagerImpl;
import entity.Row;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.rmi.RemoteException;
import java.util.List;

@RestController
@RequestMapping("/intersection")
public class IntersectionController {
    private static DatabaseManagerImpl databaseManager;

    static {
        try {
            databaseManager = DatabaseManagerImpl.getInstance();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @GetMapping
    public ResponseEntity<List<Row>> createDatabase(@RequestParam("firstTableName") String firstTableName,
                                                    @RequestParam("secondTableName") String secondTableName,
                                                    @RequestParam("firstColumnName") String firstColumnName,
                                                    @RequestParam("secondColumnName") String secondColumnName) {
        return ResponseEntity.ok(databaseManager.tablesIntersection(firstTableName,
            secondTableName,
            firstColumnName,
            secondColumnName));
    }
}
