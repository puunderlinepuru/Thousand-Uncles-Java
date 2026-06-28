package com.thousand_uncles.discord_bot.bot.util;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Random;

@Component
public class GlobalThings {
    private static Random rand;
    private static boolean appLocked = false;
    private static int pets = 0;

    GlobalThings(){
        System.out.println("Generating random sequence seed...");
        rand = new Random();
    }

    private static final List<String> mapIDS = (List<String>) YAMLHandler.yamlRead("shared_resources/maps.yaml").get("maps");

    public static List<String> getMapIDS() {return mapIDS;}

    @SuppressWarnings("unused")
    public static Integer getMapID(String mapName) {return mapIDS.indexOf(mapName);}

    public static Random getRand() {return rand;}

    public static boolean isAppLocked() {
        return appLocked;
    }

    public static void setAppLocked(boolean isAppLocked) {
        GlobalThings.appLocked = isAppLocked;
    }

    public static void setPets(int pets) {
        GlobalThings.pets = pets;
    }

    public static int getPets() {
        return pets;
    }
}
