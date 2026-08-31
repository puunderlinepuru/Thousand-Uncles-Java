package com.thousand_uncles.discord_bot.bot.services;

import com.thousand_uncles.discord_bot.common.config.BotConfig;
import com.thousand_uncles.discord_bot.common.util.GlobalThings;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.object.entity.poll.PollAnswer;
import discord4j.core.object.presence.ClientActivity;
import discord4j.core.object.presence.ClientPresence;
import discord4j.core.object.presence.Status;
import discord4j.core.spec.PollCreateSpec;
import org.springframework.stereotype.Component;

import java.util.List;

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
        setStatus();
    }


    private void signalSetup(){
        final String READY_MESSAGE = botConfig.getReady_message();

        assert client != null;
        if (READY_MESSAGE != null && !READY_MESSAGE.isEmpty()) {

            globalThings.getTheCave().createMessage().withContent(READY_MESSAGE).block();
        }
    }

    private void setStatus(){
        assert client != null;

        client.updatePresence(ClientPresence.of(Status.ONLINE, ClientActivity.playing("Creating The Torment Nexus"))).block();
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

    public void publishCurrentlyGamingPoll(int duration){
        globalThings.getCurrentlyGaming().createPoll(
                PollCreateSpec.builder()
                        .question("Test")
                        .addAllAnswers(
                                List.of(
                                    PollAnswer.of("Yes"),
                                    PollAnswer.of("No")
                                )
                        )
                        .duration(duration)
                        .allowMultiselect(false)
                        .build()
        ).block();
    }

    public void publishCavePoll(int duration){
        globalThings.getTheCave().createPoll(
                PollCreateSpec.builder()
                        .question("Test")
                        .addAllAnswers(
                                List.of(
                                        PollAnswer.of("Yes"),
                                        PollAnswer.of("No")
                                )
                        )
                        .duration(duration)
                        .allowMultiselect(false)
                        .build()
        ).block();
    }
}
