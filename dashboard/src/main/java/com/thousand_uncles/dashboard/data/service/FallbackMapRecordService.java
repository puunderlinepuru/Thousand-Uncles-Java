package com.thousand_uncles.dashboard.data.service;

import com.thousand_uncles.dashboard.data.models.MapRecord;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FallbackMapRecordService {


    // Read record by name
    public MapRecord getRecordByName(String mapName) {
        return null;
    }

    // Read all records
    public List<MapRecord> getAllRecords() {
        return null;
    }

    // Update record by name
    public boolean updateRecord(String oldName, String newName) {
        return false;
    }

    // Delete record by name
    public boolean deleteRecord(String mapName) {
        return false;
    }

    // Search records by partial name
    public List<MapRecord> searchRecords(String partialName) {
        return null;
    }
}
