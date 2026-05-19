package com.thousand_uncles.dashboard.data.config;

import com.thousand_uncles.dashboard.data.models.MapRecord;
import com.thousand_uncles.dashboard.data.service.FallbackMapRecordService;
import com.thousand_uncles.dashboard.data.service.MapRecordService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.List;

@Configuration
public class DatabaseConfig {

    @Bean
    @Primary
    public MapRecordService mapRecordService(FallbackMapRecordService fallbackService) {
        return new MapRecordService() {
            // Add new record
            @Override
            public MapRecord addRecord(String mapName) {
                MapRecord record = new MapRecord(mapName);
                System.out.println("Using fallback");
                return fallbackService.addRecord(mapName);
            }

            // Read record by name
            @Override
            public MapRecord getRecordByName(String mapName) {
                System.out.println("Using fallback");
                return fallbackService.getRecordByName(mapName);
            }

            // Read all records
            @Override
            public List<MapRecord> getAllRecords() {
                System.out.println("Using fallback");
                return fallbackService.getAllRecords();
            }

            // Update record by name
            @Override
            public boolean updateRecord(String oldName, String newName) {
                System.out.println("Using fallback");
                return fallbackService.updateRecord(oldName, newName);
            }

            // Delete record by name
            @Override
            public boolean deleteRecord(String mapName) {
                System.out.println("Using fallback");
                return fallbackService.deleteRecord(mapName);
            }

            // Search records by partial name
            @Override
            public List<MapRecord> searchRecords(String partialName) {
                System.out.println("Using fallback");
                return fallbackService.searchRecords(partialName);
            }
        };
    }
}
