package com.example.demo.controller;

import entity.DatabaseManagerImpl;
import entity.Table;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.rmi.RemoteException;

@RestController
@RequestMapping("/table")
public class TableController {
    private static DatabaseManagerImpl databaseManager;

    static {
        try {
            databaseManager = DatabaseManagerImpl.getInstance();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @GetMapping("{name}")
    public ResponseEntity<Table> getTable(@PathVariable String name) {
        return ResponseEntity.ok(databaseManager.getTable(name));
    }

    @PostMapping
    public ResponseEntity<Table> addTable(@RequestParam("name") String name) {
        databaseManager.addTable(name);
        return ResponseEntity.ok(databaseManager.getTable(name));
    }

    @DeleteMapping("{name}")
    public ResponseEntity<String> deleteTable(@PathVariable String name) {
        databaseManager.deleteTable(name);
        return ResponseEntity.ok("table: " + name + " was deleted");
    }
}
