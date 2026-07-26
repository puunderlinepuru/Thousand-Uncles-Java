package com.thousand_uncles.discord_bot.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;

public class JSONHandler {

    public static String convertJSONtoString(JsonNode jsonNode) throws JsonProcessingException {
        String jsonString = GlobalThings.getObjectMapper().writeValueAsString(jsonNode);
        return null;
    }

    public static JsonNode processJSON(String message) throws JsonProcessingException {
        return GlobalThings.getObjectMapper().readTree(message);
    }
}
