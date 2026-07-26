package com.thousand_uncles.discord_bot.commands;

import com.thousand_uncles.discord_bot.fun_stuff.Roulette;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@SuppressWarnings("unused")
@Component
public class GambaCommand implements SlashCommand{
    @Override
    public String getName() {
        return "gamba";
    }

    /**
     * Roulette flow:
     * 1 - Roulette.getRouletteMenu - Prints all available bet types with select menu
     * 2 - InteractionListener -> Roulette.handleSet for selectedOption;
     * 3 - Roulette.handleSet -> Roulette.TypeHandlers.set[type] - presents Modal
     * 4 - ModalListener -> ModalListener.RouletteSubmissionHandlers - gets data
     * 5 - ModalListener.RouletteSubmissionHandlers -> Roulette.Rolls.[type] - calls roll and gets [number rolled, W/L, payout amount]
     * 6 - ModalListener.RouletteSubmissionHandlers gets result and prints out.
     */
    @Override
    public Mono<Void> handle(ChatInputInteractionEvent event) {
        return Roulette.getRouletteMenu(event);

//        return Roulette.Rolls.rollStraightUp()
    }


}
