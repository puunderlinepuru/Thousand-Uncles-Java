package com.thousand_uncles.discord_bot.listeners;

import com.thousand_uncles.discord_bot.fun_stuff.*;
import discord4j.common.util.Snowflake;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.event.domain.message.MessageEvent;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.channel.Channel;
import discord4j.core.object.entity.channel.MessageChannel;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Dictionary;


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
            if (!message.getUserMentionIds().contains(client.getSelfId())) {return Mono.empty();}
            String response;
            if (message.getContent().contains("?")
                    || message.getContent().contains("is it")
                    || message.getContent().contains("is this")
                    || message.getContent().contains("is that"))
            {
                response = Magic_8_ball.getAnswers();
            }

            else {
                response = RandomDictionary.getWisdom();
            }
            return message.getChannel()
                    .flatMap(channel -> channel.createMessage(response))
                    .then();


            /*return message.getChannel()
                    .flatMap(channel -> channel.createMessage("You said: " + message.getContent()))
                    .then();*/
        }
        return Mono.empty();
    }
}