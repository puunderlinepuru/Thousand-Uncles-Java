package com.thousand_uncles.dashboard.controllers.api;

import com.thousand_uncles.data.models.MapRecord;
import com.thousand_uncles.data.service.MapRecordServiceProd;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/maps")
@SuppressWarnings("unused")
public class MapRecordController {

    @Autowired
    private MapRecordServiceProd mapRecordServiceProd;

    // Add new record
    @PostMapping
    public ResponseEntity<MapRecord> addRecord(@RequestBody MapRecord record) {
        MapRecord saved = mapRecordServiceProd.addRecord(record);
        return ResponseEntity.ok(saved);
    }

    // Get record by name
    @GetMapping("/{name}")
    public ResponseEntity<MapRecord> getRecord(@PathVariable String name) {
        MapRecord record = mapRecordServiceProd.searchRecords(name, "any").getFirst();
        if (record != null) {
            return ResponseEntity.ok(record);
        }
        return ResponseEntity.notFound().build();
    }

    // Search records
    @GetMapping("/search/{partialName}")
    public ResponseEntity<List<MapRecord>> searchRecords(@PathVariable String partialName) {
        List<MapRecord> records = mapRecordServiceProd.searchRecords(partialName, "any");
        return ResponseEntity.ok(records);
    }
}
