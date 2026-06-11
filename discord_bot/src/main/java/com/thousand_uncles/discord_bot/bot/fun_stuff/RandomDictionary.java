package com.thousand_uncles.discord_bot.bot.fun_stuff;

import com.thousand_uncles.discord_bot.bot.util.GlobalThings;
import com.thousand_uncles.discord_bot.bot.util.YamlReader;

import java.util.List;

public class RandomDictionary {
    public static String getWisdom(){
        List<String> dictionary = (List<String>) YamlReader.yamlRead("resources/dictionary.yml").get("words");
        int n = GlobalThings.getRand().nextInt(dictionary.size());
        System.out.println("rolled - " + dictionary.get(n));
        return dictionary.get(n);
    }
}
