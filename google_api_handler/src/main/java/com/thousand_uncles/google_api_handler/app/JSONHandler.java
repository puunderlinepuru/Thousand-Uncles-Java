package com.thousand_uncles.google_api_handler.app;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Set;

public class JSONHandler {
    static final Set<String> gamemodes = Set.of("capture point", "territory control", "capture the flag", "koth", "payload");

    public static JsonNode readRecordsJSON (String fileName) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(new File("shared/" + fileName));
        return jsonNode;
    }

    public static void writeRecordsJSON(String fileName, List<List<String>> records) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode jsonNode = objectMapper.createObjectNode();
        for (int i = 0; i < records.size(); i++) {
            jsonNode.set((String) records.get(i).get(0), formatNode(records.get(i)));
        }
        try {
            objectMapper
                    .writerWithDefaultPrettyPrinter()
                    .writeValue(new File("shared/" + fileName), jsonNode);
            System.out.println("Records JSON updated");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static JsonNode beatenRecords(JsonNode oldRecordsNode, List<List<String>> newRecordsTable) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode beatenRecords = objectMapper.createObjectNode();

        for (List<String> newMapRecord : newRecordsTable) {
            String mapName = (String) newMapRecord.getFirst();
            System.out.println("map: " + mapName);

            System.out.println("getting time for " + mapName);
            String[] timeStringParts;
            int minutes;
            int seconds;

            int oldTime;
            try {
                timeStringParts = oldRecordsNode.get(mapName).get("curr_time").asText().split(":");
                minutes = Integer.parseInt(timeStringParts[0]);
                seconds = Integer.parseInt(timeStringParts[1]);
                oldTime = seconds + minutes*60;

                int newTime;
                timeStringParts = newMapRecord.get(1).split(":");
                minutes = Integer.parseInt(timeStringParts[0]);
                seconds = Integer.parseInt(timeStringParts[1]);
                newTime = seconds + minutes*60;

                System.out.println(oldTime + " <-> " + newTime);

                if (oldTime > newTime) {
                    System.out.println("[ UPGRADE ]" + mapName + " has better time");
                    beatenRecords.set(mapName, formatNode(newMapRecord));
                }
            } catch (NullPointerException nullPointerException) {
                System.out.println("[ NEW MAP ]" + mapName);
                beatenRecords.set(mapName, formatNode(newMapRecord));
            }
        }
        return beatenRecords;
    }

    private static ObjectNode formatNode(List<String> mapRecord){
        ObjectNode mapNode = new ObjectMapper().createObjectNode();
        if (mapRecord.toArray().length >= 4) {
            mapNode.put("curr_time", mapRecord.get(1));
            mapNode.put("prev_time", mapRecord.get(2));
            mapNode.put("image_proof1_link", mapRecord.get(3));

        }
        if (mapRecord.toArray().length >= 6) {
            mapNode.put("image_proof2_link", mapRecord.get(4));
            mapNode.put("image_proof3_link", mapRecord.get(5));
        }
        if (mapRecord.toArray().length == 7) {
            System.out.println(mapRecord);
            mapNode.put("video_proof_link", mapRecord.get(6));
        }

        return mapNode;
    }

}
