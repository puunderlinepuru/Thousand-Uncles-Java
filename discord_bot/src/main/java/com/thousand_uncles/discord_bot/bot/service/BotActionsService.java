package com.thousand_uncles.discord_bot.bot.service;

import com.thousand_uncles.discord_bot.bot.config.BotConfig;
import com.thousand_uncles.discord_bot.bot.util.GlobalThings;
import discord4j.core.GatewayDiscordClient;
import org.springframework.stereotype.Component;

@SuppressWarnings("unused")
@Component
public class BotActionsService {
    GatewayDiscordClient client;
    BotConfig botConfig;
    GlobalThings globalThings;

    BotActionsService(GatewayDiscordClient client, BotConfig botConfig, GlobalThings globalThings){
        this.client = client;
        this.botConfig = botConfig;
        this.globalThings = globalThings;
        signalSetup();
    }


    private void signalSetup(){
        final String READY_MESSAGE = botConfig.getReady_message();

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
