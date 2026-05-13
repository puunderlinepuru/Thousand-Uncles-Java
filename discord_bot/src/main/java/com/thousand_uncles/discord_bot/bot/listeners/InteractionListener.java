package com.thousand_uncles.discord_bot.bot.listeners;

import com.thousand_uncles.discord_bot.bot.util.BotResponseFormatter;
import com.thousand_uncles.discord_bot.data.models.MapRecord;
import com.thousand_uncles.discord_bot.data.service.MapRecordService;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.interaction.ButtonInteractionEvent;
import discord4j.core.event.domain.interaction.SelectMenuInteractionEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@SuppressWarnings("unused")
@Component
public class InteractionListener {

    @Autowired
    ApplicationContext applicationContext;

    GatewayDiscordClient client;

    public InteractionListener(GatewayDiscordClient client) {
        this.client = client;

        client.on(ButtonInteractionEvent.class, this::onButton).subscribe();

        client.on(SelectMenuInteractionEvent.class, this::onSelectMenu).subscribe();
    }

    public Mono<Void> onButton (ButtonInteractionEvent event){
        System.out.println("button");

        return Mono.empty();
    }

    public Mono<Void> onSelectMenu (SelectMenuInteractionEvent event){
        System.out.println("select menu");
        System.out.println(event.getValues().getFirst());

        MapRecordService mapRecordService = applicationContext.getBean(MapRecordService.class);
        MapRecord gotMap = mapRecordService.getRecordByName(event.getValues().getFirst());
        System.out.println("found map " + gotMap.getMap_name());

        assert event.getMessage().isPresent();
        System.out.println(event.getMessage().get().getChannel().block());
        event.edit().withContent(BotResponseFormatter.getResponse(gotMap)).withComponents().block();

        return Mono.empty();
    }


}
