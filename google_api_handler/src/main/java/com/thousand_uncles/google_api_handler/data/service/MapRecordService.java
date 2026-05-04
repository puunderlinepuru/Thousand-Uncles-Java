package com.thousand_uncles.google_api_handler.data.service;

import com.thousand_uncles.google_api_handler.data.models.MapRecord;
import com.thousand_uncles.google_api_handler.data.repository.MapRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class MapRecordService {

    @Autowired
    @SuppressWarnings("unused")
    private MapRecordRepository repository;

    // Add new record
    public MapRecord addRecord(String map_name, short curr_wr_seconds) {
        MapRecord record = new MapRecord(map_name, curr_wr_seconds);
        return repository.save(record);
    }

    // Read record by name
    public MapRecord getRecordByName(String mapName) {
        return repository.findByMap_name(mapName);
    }

    // Read all records
    public List<MapRecord> getAllRecords() {
        return repository.findAll();
    }

    // Update record by name
    public boolean updateMapName(String oldName, String newName) {
        int updated = repository.updateMap_name(oldName, newName);
        return updated > 0;
    }

    // Delete record by name
    public boolean deleteRecord(String mapName) {
        int deleted = repository.deleteByMap_name(mapName);
        return deleted > 0;
    }

//    Modify existing WR
    public boolean updateWR(String mapName, int newWR){
        int updated = repository.updateCurr_wr_seconds(mapName, newWR);
        return updated > 0;
    }

    // Search records by partial name
    public List<MapRecord> searchRecords(String partialName) {
        return repository.findByMap_nameContaining(partialName);
    }
}