package com.thousand_uncles.dashboard.data.service;

import com.thousand_uncles.dashboard.data.models.MapRecord;
import com.thousand_uncles.dashboard.data.repository.MapRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@Profile("prod")
public class MapRecordService {

    @Autowired(required = false)
    @SuppressWarnings("unused")
    private MapRecordRepository repository;

    // Add new record
    public MapRecord addRecord(Integer id, String mapName) {
        MapRecord record = new MapRecord(id, mapName);
        return repository.save(record);
    }

    // Read record by name
    public MapRecord getRecordByName(String mapName) {
        return repository.findByMapName(mapName);
    }

    // Read all records
    public List<MapRecord> getAllRecords() {
        return repository.findAll();
    }

    // Update record by name
    public boolean updateRecord(String oldName, String newName) {
        int updated = repository.updateMapName(oldName, newName);
        return updated > 0;
    }

    // Delete record by name
    public boolean deleteRecord(String mapName) {
        int deleted = repository.deleteByMapName(mapName);
        return deleted > 0;
    }

    // Search records by partial name
    public List<MapRecord> searchRecords(String partialName) {
        return repository.findByMapNameContaining(partialName);
    }
}