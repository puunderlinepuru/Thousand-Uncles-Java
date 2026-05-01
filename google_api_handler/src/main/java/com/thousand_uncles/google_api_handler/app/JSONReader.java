package com.thousand_uncles.google_api_handler.app;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Set;

public class JSONReader {
    private static final Set<String> gamemodes = Set.of("capture point", "territory control", "capture the flag", "koth", "payload");

    public static void main(String[] args) {
        readRecordsJSON("records.json");
    }

    public static JsonNode readRecordsJSON(String fileName) {
        try {

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(new File("shared/" + fileName));
            System.out.println("JsonNode: " + jsonNode);
            System.out.println("item at 0: " + jsonNode.get(0));
            System.out.println("3: " + jsonNode.get("cp_altitude"));
            System.out.println("size: " + jsonNode.size());

            for (Object map : jsonNode) {
                if (map instanceof ObjectNode){
                    System.out.println("test: " + ((ObjectNode) map).get("curr_time"));
                    System.out.println(((ObjectNode) map).);
                }
                System.out.println("loop test: " + map.getClass());
//                System.out.println(map);
            }
            return jsonNode;

        } catch (Exception e) {
            System.out.println("[ERROR]" + e);
        }
        return null;
    }

    public static void writeRecordsJSON(String fileName, List<List<Object>> records) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode jsonNode = objectMapper.createObjectNode();


        for (int i = 0; i < records.size(); i++) {
            ObjectNode mapNode = objectMapper.createObjectNode();
            if (records.get(i).toArray().length < 4 && !gamemodes.contains(records.get(i).get(0).toString())) {
                System.out.println("error in element " + records.get(i) + " at " + i);
            }
            if (records.get(i).toArray().length >= 4) {
                mapNode.put("curr_time", (String) records.get(i).get(1));
                mapNode.put("prev_time", (String) records.get(i).get(2));
                mapNode.put("image_proof1_link", (String) records.get(i).get(3));

            }
            if (records.get(i).toArray().length >= 6) {
                mapNode.put("image_proof2_link", (String) records.get(i).get(4));
                mapNode.put("image_proof3_link", (String) records.get(i).get(5));
            }
            if (records.get(i).toArray().length == 7) {
                System.out.println(records.get(i));
                mapNode.put("video_proof_link", (String) records.get(i).get(6));
            }
            jsonNode.set((String) records.get(i).get(0), mapNode);
//            System.out.println(values.get(i));

        }

        System.out.println(records.size());

        try {
            objectMapper
                    .writerWithDefaultPrettyPrinter()
                    .writeValue(new File("shared/" + fileName), jsonNode);
            System.out.println("Records JSON updated");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static JsonNode beatenRecords(JsonNode oldNode, ObjectNode newNode) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode beatenRecords = objectMapper.createObjectNode();

        if (oldNode.size() < newNode.size()) {
//            New maps?
            for (Object map : newNode) {
                if (map instanceof ObjectNode){
                    System.out.println("test: " + ((ObjectNode) map).get("curr_time"));
                    System.out.println(((ObjectNode) map).asText());
                }
                System.out.println("loop test: " + map.getClass());
//                System.out.println(map);
            }
        }


        return null;
    }

}
