package com.thousand_uncles.discord_bot.bot.listeners;

import com.thousand_uncles.discord_bot.bot.fun_stuff.Roulette;
import com.thousand_uncles.discord_bot.bot.util.Config;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.interaction.ModalSubmitInteractionEvent;
import discord4j.core.object.component.*;
import discord4j.core.spec.InteractionApplicationCommandCallbackSpec;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
@Component
public class ModalListenerProd {
    GatewayDiscordClient client;

    ModalListenerProd(GatewayDiscordClient client){
        this.client = client;

        client.on(ModalSubmitInteractionEvent.class, this::onModalSubmit).subscribe();
    }

    public Mono<Void> onModalSubmit(ModalSubmitInteractionEvent event){
        String[] customIdParts = event.getCustomId().split("-");

        if (customIdParts[0].equals("roulette")){
            System.out.println("got submission:");
            switch (customIdParts[1]){
                case "straightup":
                    System.out.println("Straight Up");

                    int betNumber = -1, betAmount = -1;

                    List<TextInput> textInputComponents = event.getComponents(TextInput.class);

                    if (textInputComponents.isEmpty()) {
                        return event.reply("No components found!");
                    }

                    for (TextInput component : textInputComponents) {
                        if (component.getCustomId().equals("betNumber")){
                            betNumber = Integer.parseInt(component.getValue().orElse("-1"));
                        } else if (component.getCustomId().equals("betAmount")) {
                            betAmount = Integer.parseInt(component.getValue().orElse("-1"));
                        }
                    }

                    if (betAmount == -1 | betNumber == -1){
                        return event.reply()
                                .withEphemeral(true)
                                .withContent("Error converting numbers. Try again :p");
                    } else {
                        return Roulette.Rolls.rollStraightUp(event, betNumber);
                    }
            }
            List<ICanBeUsedInContainerComponent> firstComponents = new ArrayList<>();
            firstComponents.add(TextDisplay.of(String.format("Comments: %s", "comments")));


            Container container = Container.of(firstComponents);

            return event.reply(InteractionApplicationCommandCallbackSpec.builder().addComponent(container).build());
        }

        return Mono.empty();
    }
}
