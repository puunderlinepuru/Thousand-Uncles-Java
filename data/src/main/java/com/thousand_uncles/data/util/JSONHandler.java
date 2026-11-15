package com.thousand_uncles.data.util;

import org.springframework.stereotype.Component;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

@Component
public class JSONHandler {

    private final ObjectMapper objectMapper;

    JSONHandler(ObjectMapper objectMapper){
        this.objectMapper = objectMapper;
    }

    public String convertJSONtoString(JsonNode jsonNode) {
        String jsonString = objectMapper.writeValueAsString(jsonNode);
        return null;
    }

    public JsonNode processJSON(String message){
        return objectMapper.readTree(message);
    }
}
