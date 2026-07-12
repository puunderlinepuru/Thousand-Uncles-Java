package com.thousand_uncles.discord_bot.bot.util;

import com.thousand_uncles.discord_bot.bot.config.Config;
import discord4j.common.util.Snowflake;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.object.entity.Guild;
import discord4j.core.object.entity.channel.MessageChannel;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Random;

@Component
public class GlobalThings {
    private static Random rand;
    private static boolean appLocked = false;
    private static int pets = 0;
    private static MessageChannel theCave;
    private static MessageChannel currentlyGaming;
    GatewayDiscordClient client;
    Config config;

    GlobalThings(GatewayDiscordClient client, Config config){
        this.client = client;
        this.config = config;


        System.out.println("Generating random sequence seed...");
        rand = new Random();

        final String SERVER_ID = config.getServer_id();
        final String THE_CAVE_CHANNEL_ID = config.getThe_cave_channel_id();
        final String CURRENTLY_GAMING_CHANNEL_ID = config.getCurrently_gaming_channel_id();

        Guild server = client.getGuildById(Snowflake.of(SERVER_ID)).block();
        assert server != null;
        theCave = (MessageChannel) server.getChannelById(Snowflake.of(THE_CAVE_CHANNEL_ID)).block();
        currentlyGaming = (MessageChannel) server.getChannelById(Snowflake.of(CURRENTLY_GAMING_CHANNEL_ID)).block();
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

    public MessageChannel getTheCave() {
        return theCave;
    }

    public MessageChannel getCurrentlyGaming() {
        return currentlyGaming;
    }
}
