package com.thousand_uncles.data.util;

import com.thousand_uncles.data.util.YAMLHandler;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

@SuppressWarnings("unused")
public class RecognizedMapsChecker {

    public static List<String> checkMapListValidity() {
        final String MAPCYCLE_TXT = "resources/mapcycle.txt";
        final String MAIN_MAPS_YAML = "../shared_resources/maps.yaml";

        IO.println("Comparing: \n" +
                "mapcycle.txt: " + MAPCYCLE_TXT + "\n" +
                "Tracked maps: " + MAIN_MAPS_YAML);
        try {
            Set<String> toBeProcessed = readWordsFromFile(MAPCYCLE_TXT);


            List<String> trackedMaps = (List<String>) YAMLHandler.yamlRead(MAIN_MAPS_YAML).get("maps");
            Map<String, String> mapSuffixMap = (Map<String, String>) YAMLHandler.yamlRead(MAIN_MAPS_YAML).get("map_suffixes");
            List<String> mapSuffixes = new ArrayList<>(mapSuffixMap.keySet());
            trackedMaps.addAll(mapSuffixes);
            return compareAndPrintMissing(toBeProcessed, trackedMaps);

        } catch (IOException e) {
            System.err.println("Error reading files: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
    
    private static Set<String> readWordsFromFile(String filePath) throws IOException {
        Set<String> maps = new HashSet<>();

        // Check if file exists first (optional but good practice)
        Files.notExists(Paths.get(filePath));

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Trim whitespace and ignore empty lines
                String map = line.trim();
                if (!map.isEmpty()) {
                    maps.add(map);
                }
            }
        }
        return maps;
    }

    private static List<String> compareAndPrintMissing(Set<String> mapCycleMaps, List<String> trackedMaps) {
        IO.println("=== Maps not yet processed ===");

        int count = 0;
        List<String> missingMaps = new ArrayList<>();
        for (String map : mapCycleMaps) {
            if (!trackedMaps.contains(map)) {
                IO.println("- " + map);
                count++;
                missingMaps.add(map);
            }
        }

        if (count == 0) {
            IO.println("All maps have been processed.");
        } else {
            IO.println("\nTotal missing: " + count);
            IO.println("Run updater or update child maps YAML files for Development");
        }
        return missingMaps;
    }
}
