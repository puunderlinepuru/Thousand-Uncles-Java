package com.thousand_uncles.google_api_handler.data.config;

import com.thousand_uncles.google_api_handler.data.service.FallbackMapRecordService;
import com.thousand_uncles.google_api_handler.data.service.MapRecordServiceProd;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@SuppressWarnings("unused")
public class DatabaseConfig {

    @SuppressWarnings("unused")
    @Bean
    @Profile("dev")
    public MapRecordServiceProd mapRecordService(FallbackMapRecordService fallbackService) {
        /*return new MapRecordServiceProd() {
            // Add new record

            // Read record by name
            *//*@Override
            public MapRecord getRecordByName(String mapName) {
                System.out.println("Using fallback");
                return fallbackService.getRecordByName(mapName);
            }*//*

            // Read all records
            *//*@Override
            public List<MapRecord> getAllRecords() {
                System.out.println("Using fallback");
                return fallbackService.getAllRecords();
            }*//*

            // Delete record by name
            public boolean deleteRecord(String mapName) {
                System.out.println("Using fallback");
                return fallbackService.deleteRecord(mapName);
            }

            // Search records by partial name
            public List<MapRecord> searchRecords(String partialName) {
                System.out.println("Using fallback");
                return fallbackService.searchRecords(partialName);
            }
        };*/
        return null;
    }
}

