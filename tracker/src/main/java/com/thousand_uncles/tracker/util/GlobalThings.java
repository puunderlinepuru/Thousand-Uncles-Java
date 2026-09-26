package com.thousand_uncles.tracker.util;

import tools.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Random;

@Component
public class GlobalThings {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    GlobalThings(){}

    private static final List<String> mapIDS = (List<String>) YAMLHandler.yamlRead("shared_resources/maps.yaml").get("maps");
    private static final Map<String, String> mapSuffixes = (Map<String, String>) YAMLHandler.yamlRead("shared_resources/maps.yaml").get("map_suffixes");

    public static List<String> getMapIDS() {return mapIDS;}

    public static Map<String, String> getMapSuffixes() {return mapSuffixes;}

    public static ObjectMapper getObjectMapper() {return objectMapper;}
}
