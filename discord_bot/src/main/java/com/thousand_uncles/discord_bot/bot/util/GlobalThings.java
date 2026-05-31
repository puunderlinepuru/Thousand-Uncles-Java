package com.thousand_uncles.discord_bot.bot.util;

import com.thousand_uncles.discord_bot.bot.YamlReader;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Random;

@Component
public class GlobalThings {
    public static Random rand;
    private static final List<String> mapIDS = (List<String>) YamlReader.yamlRead("resources/maps.yaml").get("maps");

    public static List<String> getMapIDS() {return mapIDS;}

    public static Integer getMapID(String mapName) {return mapIDS.indexOf(mapName);}

    public static Random getRand() {return rand;}
}
