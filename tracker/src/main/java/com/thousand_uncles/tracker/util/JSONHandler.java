package com.thousand_uncles.tracker.util;


import tools.jackson.databind.JsonNode;

public class JSONHandler {

    public static String convertJSONtoString(JsonNode jsonNode) {
        String jsonString = GlobalThings.getObjectMapper().writeValueAsString(jsonNode);
        return null;
    }

    public static JsonNode processJSON(String message){
        return GlobalThings.getObjectMapper().readTree(message);
    }
}
