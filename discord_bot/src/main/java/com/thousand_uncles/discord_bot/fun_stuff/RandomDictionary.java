package com.thousand_uncles.discord_bot.fun_stuff;

import com.thousand_uncles.discord_bot.util.GlobalThings;
import com.thousand_uncles.discord_bot.util.YAMLHandler;

import java.util.List;
import java.util.Optional;

public class RandomDictionary {
    public static Optional<String> getWisdom(){
        List<String> dictionary = (List<String>) YAMLHandler.yamlRead("resources/dictionary.yml").get("words");
        int n = GlobalThings.getRand().nextInt(dictionary.size());
        if (n == 5){
            return Optional.empty();
        }
        String rolled = dictionary.get(n);
        return rolled.describeConstable();
    }
}
