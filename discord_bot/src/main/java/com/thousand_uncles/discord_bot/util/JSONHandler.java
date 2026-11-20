package com.thousand_uncles.discord_bot.util;


import com.thousand_uncles.discord_bot.common.util.GlobalThings;
import tools.jackson.databind.JsonNode;

public class JSONHandler {

    public static String convertJSONtoString(JsonNode jsonNode) {
        return GlobalThings.getObjectMapper().writeValueAsString(jsonNode);
    }

    public static JsonNode processJSON(String message){
        return GlobalThings.getObjectMapper().readTree(message);
    }
}
