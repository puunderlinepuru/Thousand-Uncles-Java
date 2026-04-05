package com.thousand_uncles.discord_bot.listeners;

import com.thousand_uncles.discord_bot.commands.SlashCommand;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Message;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class MessageListener {

    GatewayDiscordClient client;

    public MessageListener(GatewayDiscordClient client) {

        this.client = client;

        client.on(MessageCreateEvent.class, this::onMessage).subscribe();
    }

    public Mono<Void> onMessage(MessageCreateEvent event) {
        final Message message = event.getMessage();
        if (message.getAuthor().map(user -> !user.isBot()).orElse(false)) {
            System.out.println("mentions: \n" +message.getUserMentionIds().contains(client.getSelfId()));
            return message.getChannel()
                    .flatMap(channel -> channel.createMessage("You said: " + message.getContent()))
                    .then();
        }
        return Mono.empty();
    }
}