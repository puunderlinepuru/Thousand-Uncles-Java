package com.thousand_uncles.data_controller.config;

import com.thousand_uncles.data.util.YAMLHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tools.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Map;

@Configuration
public class GlobalConfigThings {

    @Bean
    public ObjectMapper ObjectMapper(){
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper;
    }

    @Bean
    public List<String> mapIDS(){
        List<String> mapIDS = (List<String>) YAMLHandler.yamlRead("shared_resources/maps.yaml").get("maps");
        return mapIDS;
    }
    @Bean
    public Map<String, String> mapSuffixes(){
        Map<String, String> mapSuffixes = (Map<String, String>) YAMLHandler.yamlRead("shared_resources/maps.yaml").get("map_suffixes");
        return mapSuffixes;
    }
}
