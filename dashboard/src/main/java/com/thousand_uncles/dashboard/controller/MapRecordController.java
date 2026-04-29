package com.thousand_uncles.dashboard.controller;

import com.thousand_uncles.dashboard.entity.MapRecord;
import com.thousand_uncles.dashboard.service.MapRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/maps")
@SuppressWarnings("unused")
public class MapRecordController {

    @Autowired
    private MapRecordService service;

//    // Add new record
//    @PostMapping
//    public ResponseEntity<MapRecord> addRecord(@RequestBody MapRecord record) {
//        MapRecord saved = service.addRecord(record.getMapName());
//        return ResponseEntity.ok(saved);
//    }

    // Get record by name
    @GetMapping("/{name}")
    public ResponseEntity<MapRecord> getRecord(@PathVariable String name) {
        MapRecord record = service.getRecordByName(name);
        if (record != null) {
            return ResponseEntity.ok(record);
        }
        return ResponseEntity.notFound().build();
    }

    // Get all records
    @GetMapping
    public ResponseEntity<List<MapRecord>> getAllRecords() {
        List<MapRecord> records = service.getAllRecords();
        return ResponseEntity.ok(records);
    }

    // Update record
    @PutMapping("/{oldName}")
    public ResponseEntity<MapRecord> updateRecord(
            @PathVariable String oldName,
            @RequestBody MapRecord updatedRecord) {
        if (service.updateRecord(oldName, updatedRecord.getMapName())) {
            MapRecord record = service.getRecordByName(updatedRecord.getMapName());
            return ResponseEntity.ok(record);
        }
        return ResponseEntity.notFound().build();
    }

    // Delete record
    @DeleteMapping("/{name}")
    public ResponseEntity<Void> deleteRecord(@PathVariable String name) {
        if (service.deleteRecord(name)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    // Search records
    @GetMapping("/search/{partialName}")
    public ResponseEntity<List<MapRecord>> searchRecords(@PathVariable String partialName) {
        List<MapRecord> records = service.searchRecords(partialName);
        return ResponseEntity.ok(records);
    }
}
