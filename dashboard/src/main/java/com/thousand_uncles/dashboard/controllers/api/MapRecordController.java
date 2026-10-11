package com.thousand_uncles.dashboard.controllers.api;

import com.thousand_uncles.data.models.common.ManualIndexedMapRecordEntry;
import com.thousand_uncles.data.service.MapRecordServiceOnline;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/maps")
@SuppressWarnings("unused")
public class MapRecordController {

    @Autowired
    private MapRecordServiceOnline mapRecordServiceOnline;

    // Add new record
    @PostMapping
    public ResponseEntity<ManualIndexedMapRecordEntry> addRecord(@RequestBody ManualIndexedMapRecordEntry record) {
        ManualIndexedMapRecordEntry saved = mapRecordServiceOnline.addRecord(record);
        return ResponseEntity.ok(saved);
    }

    // Get record by name
    @GetMapping("/{name}")
    public ResponseEntity<ManualIndexedMapRecordEntry> getRecord(@PathVariable String name) {
        ManualIndexedMapRecordEntry record = mapRecordServiceOnline.searchRecords(name, "any").getFirst();
        if (record != null) {
            return ResponseEntity.ok(record);
        }
        return ResponseEntity.notFound().build();
    }

    // Search records
    @GetMapping("/search/{partialName}")
    public ResponseEntity<List<ManualIndexedMapRecordEntry>> searchRecords(@PathVariable String partialName) {
        List<ManualIndexedMapRecordEntry> records = mapRecordServiceOnline.searchRecords(partialName, "any");
        return ResponseEntity.ok(records);
    }
}
