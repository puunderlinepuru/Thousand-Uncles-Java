package com.thousand_uncles.google_api_handler.util;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GlobalThings {
    private static final List<String> mapIDS = (List<String>) YAMLHandler.yamlRead("shared_resources/maps.yaml").get("maps");

    public static List<String> getMapIDS() {return mapIDS;}

    public static Integer getMapID(String mapName) {return mapIDS.indexOf(mapName);}
}
