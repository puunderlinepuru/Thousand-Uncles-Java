package com.thousand_uncles.discord_bot.data.config;

import com.thousand_uncles.discord_bot.data.models.MapRecord;
import com.thousand_uncles.discord_bot.data.service.FallbackMapRecordService;
import com.thousand_uncles.discord_bot.data.service.MapRecordService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import java.util.List;

@Configuration
@SuppressWarnings("unused")
public class DatabaseConfig {

    @Bean
    @SuppressWarnings("unused")
    @Profile("dev")
    public MapRecordService mapRecordService(FallbackMapRecordService fallbackService) {
        return new MapRecordService() {
            // Add new record

            // Read record by name
            /*@Override
            public MapRecord getRecordByName(String mapName) {
                System.out.println("Using fallback");
                return fallbackService.getRecordByName(mapName);
            }*/

            // Read all records
            /*@Override
            public List<MapRecord> getAllRecords() {
                System.out.println("Using fallback");
                return fallbackService.getAllRecords();
            }*/

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
        };
    }
}

