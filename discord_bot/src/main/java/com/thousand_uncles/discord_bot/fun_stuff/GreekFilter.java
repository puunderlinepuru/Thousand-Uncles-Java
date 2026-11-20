package com.thousand_uncles.discord_bot.fun_stuff;

import org.springframework.stereotype.Component;

import java.io.*;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class GreekFilter {
    private Set<String> allowedWords = new HashSet<>();

    GreekFilter(){
        BufferedReader reader;
        try{
            File GILGAMESH_PATH = new File("resources/gilgamesh.txt");
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(GILGAMESH_PATH)));
            for (String line = reader.readLine();  line != null;  line = reader.readLine()) {
                String[] lineWords = line.split("\\s+");
                allowedWords.addAll(Arrays.asList(lineWords));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public Set<String> getAllowedWords() {
        return allowedWords;
    }

    public void setAllowedWords(Set<String> allowedWords) {
        this.allowedWords = allowedWords;
    }
}
