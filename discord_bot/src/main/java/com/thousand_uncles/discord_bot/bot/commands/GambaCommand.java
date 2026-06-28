package com.thousand_uncles.discord_bot.bot.commands;

import com.thousand_uncles.discord_bot.bot.fun_stuff.Roulette;
import com.thousand_uncles.discord_bot.bot.util.GlobalThings;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.object.command.ApplicationCommandInteractionOption;
import discord4j.core.object.command.ApplicationCommandInteractionOptionValue;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@SuppressWarnings("unused")
@Component
public class GambaCommand implements SlashCommand{
    @Override
    public String getName() {
        return "gamba";
    }

    @Override
    public Mono<Void> handle(ChatInputInteractionEvent event) {

        return Roulette.getRouletteMenu(event);

//        return Roulette.Rolls.rollStraightUp()
    }


}
