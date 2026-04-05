package com.thousand_uncles.discord_bot.fun_stuff;

import com.thousand_uncles.discord_bot.YamlReader;

import java.util.List;
import java.util.Random;

public class RandomDictionary {
    public static String getWisdom(){
        YamlReader yamlReader = new YamlReader("resources/dictionary.yml");
        List<String> dictionary = (List<String>) yamlReader.yamlRead().get("words");
        Random rand = new Random();
        int n = rand.nextInt(dictionary.size());
        System.out.println("rolled - " + dictionary.get(n));
        return dictionary.get(n);
    }
}
