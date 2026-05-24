package com.thousand_uncles.dashboard.data.service;

import com.thousand_uncles.dashboard.data.models.MapRecord;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Profile("dev")
public class FallbackMapRecordService {


    // Read record by name
    @SuppressWarnings("unused")
    public MapRecord getRecordByName(String mapName) {
        return null;
    }

    // Read all records
    @SuppressWarnings("unused")
    public List<MapRecord> getAllRecords() {
        return null;
    }

    // Update record by name
    @SuppressWarnings("unused")
    public boolean updateRecord(String oldName, String newName) {
        return false;
    }

    // Delete record by name
    @SuppressWarnings("unused")
    public boolean deleteRecord(String mapName) {
        return false;
    }

    // Search records by partial name
    @SuppressWarnings("unused")
    public List<MapRecord> searchRecords(String partialName) {
        return null;
    }
}
