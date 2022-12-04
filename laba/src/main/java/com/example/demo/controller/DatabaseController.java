package com.example.demo.controller;

import entity.Database;
import entity.DatabaseManagerImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.rmi.RemoteException;

@RestController
@RequestMapping("/database")
public class DatabaseController {
    private static DatabaseManagerImpl databaseManager;

    static {
        try {
            databaseManager = DatabaseManagerImpl.getInstance();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @GetMapping
    public ResponseEntity<Database> getDatabase() {
        return ResponseEntity.ok(databaseManager.getDatabase());
    }

    @PostMapping
    public ResponseEntity<Database> createDatabase(@RequestParam("name") String name) {
        return ResponseEntity.ok(databaseManager.createDatabase(name));
    }
}
