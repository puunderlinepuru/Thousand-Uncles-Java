package com.thousand_uncles.discord_bot.bot;

import com.thousand_uncles.discord_bot.bot.config.Config;
import com.thousand_uncles.discord_bot.bot.util.GlobalThings;
import discord4j.common.util.Snowflake;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.object.entity.Guild;
import discord4j.core.object.entity.channel.MessageChannel;
import org.springframework.stereotype.Component;

@SuppressWarnings("unused")
@Component
public class BotActions {
    GatewayDiscordClient client;
    Config config;
    GlobalThings globalThings;

    BotActions(GatewayDiscordClient client, Config config, GlobalThings globalThings){
        this.client = client;
        this.config = config;
        this.globalThings = globalThings;
        signalSetup();
    }


    private void signalSetup(){
        final String READY_MESSAGE = config.getReady_message();

        assert client != null;
        if (READY_MESSAGE != null && !READY_MESSAGE.isEmpty()) {

            globalThings.getTheCave().createMessage().withContent(READY_MESSAGE).block();
        }
    }

    private void timeoutSkyro(){
        assert client != null;
    }

    public void sendIntoCave(String message){
        globalThings.getTheCave().createMessage(message).block();
    }

    public void sendIntoCurrentlyGaming(String message){
        globalThings.getCurrentlyGaming().createMessage(message).block();
    }
}
