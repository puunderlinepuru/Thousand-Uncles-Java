package com.thousand_uncles.discord_bot.bot.fun_stuff;

import com.thousand_uncles.discord_bot.bot.GlobalThings;
import com.thousand_uncles.discord_bot.bot.YamlReader;

import java.util.List;

public class RandomDictionary {
    public static String getWisdom(){
        YamlReader yamlReader = new YamlReader("resources/dictionary.yml");
        List<String> dictionary = (List<String>) yamlReader.yamlRead().get("words");
        int n = GlobalThings.rand.nextInt(dictionary.size());
        System.out.println("rolled - " + dictionary.get(n));
        return dictionary.get(n);
    }
}
