package com.thousand_uncles.google_api_handler.data.config;

import com.thousand_uncles.google_api_handler.data.service.FallbackMapRecordService;
import com.thousand_uncles.google_api_handler.data.service.MapRecordServiceProd;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@SuppressWarnings("unused")
public class DatabaseConfig {

    @Bean
    @SuppressWarnings("unused")
    @Profile("dev")
    public MapRecordServiceProd mapRecordService(FallbackMapRecordService fallbackService) {

        System.out.println("fallback service initialized");
        return new MapRecordServiceProd() {
        };
    }
}

