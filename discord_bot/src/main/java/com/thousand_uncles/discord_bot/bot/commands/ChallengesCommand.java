package com.thousand_uncles.discord_bot.bot.commands;

import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@SuppressWarnings("unused")
@Component
public class ChallengesCommand implements SlashCommand{
    @Override
    public String getName() {
        return "challenges";
    }

    @Override
    public Mono<Void> handle(ChatInputInteractionEvent event) {

        String response = "https://docs.google.com/spreadsheets/d/1yNADyx6zUEmmVaiZbtzKj9ZmiENsBYGbhUkL_CTf8GY";

        return event.reply()
                .withEphemeral(true)
                .withContent(response);
    }
}
