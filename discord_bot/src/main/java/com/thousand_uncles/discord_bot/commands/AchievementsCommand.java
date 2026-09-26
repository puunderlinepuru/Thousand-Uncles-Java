package com.thousand_uncles.discord_bot.commands;

import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@SuppressWarnings("unused")
@Component
public class AchievementsCommand implements SlashCommand {
    @Override
    public String getName() {
        return "achievements";
    }

    @Override
    public Mono<Void> handle(ChatInputInteractionEvent event) {

        String response = "https://docs.google.com/document/u/0/d/1cbtkOlUSBWT8oeloZVZ5-AYd_6ILLe1mvDdc-TJz1E8/mobilebasic";

        return event.reply()
                .withEphemeral(true)
                .withContent(response);
    }
}
