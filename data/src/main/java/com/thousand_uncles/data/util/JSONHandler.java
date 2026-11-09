package com.thousand_uncles.data.util;


import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

public class JSONHandler {

    private final ObjectMapper objectMapper;

    JSONHandler(ObjectMapper objectMapper){
        this.objectMapper = objectMapper;
    }

    public static String convertJSONtoString(JsonNode jsonNode) {
        String jsonString = objectMapper.writeValueAsString(jsonNode);
        return null;
    }

    public static JsonNode processJSON(String message){
        return GlobalThings.getObjectMapper().readTree(message);
    }
}
