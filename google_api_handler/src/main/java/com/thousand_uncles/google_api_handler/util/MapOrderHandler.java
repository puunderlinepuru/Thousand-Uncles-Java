package com.thousand_uncles.google_api_handler.util;

import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Component
public class MapOrderHandler {
    private static List<String> mapOrderList = (List<String>) YAMLHandler.yamlRead("shared_resources/ordered_maps.yaml").get("map_order");

    public static void main(String[] args) {
        sortMapsToYAML();
    }

    public static void sortMapsToYAML(){
        mapOrderList = (List<String>) YAMLHandler.yamlRead("shared_resources/maps.yaml").get("maps");
        Collections.sort(mapOrderList);
        Map<String, List<String>> mapOrderMap = Map.of("map_order", mapOrderList);
        YAMLHandler.yamlWriteList("shared_resources/ordered_maps.yaml", mapOrderMap);
    }

    @SuppressWarnings("unused")
    public static void updateEverything(String sheetName){

    }

    public static List<String> getMapOrderList() {
        return mapOrderList;
    }

    @SuppressWarnings("unused")
    public static void setMapOrderList(List<String> mapOrderList) {
        MapOrderHandler.mapOrderList = mapOrderList;
    }
}
