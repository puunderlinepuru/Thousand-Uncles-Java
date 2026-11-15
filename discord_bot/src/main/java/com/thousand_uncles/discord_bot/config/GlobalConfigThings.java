package com.thousand_uncles.discord_bot.config;

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
        return new ObjectMapper();
    }

    @Bean
    public List<String> mapNames(){
        return (List<String>) YAMLHandler.yamlRead("shared_resources/maps.yaml").get("maps");
    }
    @Bean
    public Map<String, String> mapSuffixes(){
        return (Map<String, String>) YAMLHandler.yamlRead("shared_resources/maps.yaml").get("map_suffixes");
    }
}
