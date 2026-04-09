package com.thousand_uncles.discord_bot.listeners;

import com.thousand_uncles.discord_bot.YamlReader;
import com.thousand_uncles.discord_bot.fun_stuff.*;
import discord4j.common.util.Snowflake;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Message;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Map;


@Component
public class MessageListener {
    static YamlReader configReader = new YamlReader("resources/config.yml");
    static Map config = configReader.yamlRead();
    private static final String MEME_CHANNEL_ID = (String) config.get("meme_channel_id");

    GatewayDiscordClient client;

    public MessageListener(GatewayDiscordClient client) {

        client.on(MessageCreateEvent.class, this::onMessage).subscribe();
    }

    public Mono<Void> onMessage(MessageCreateEvent event) {
        final Message message = event.getMessage();
//        Allowed Channel Check
        if (!message.getChannelId().equals(Snowflake.of(MEME_CHANNEL_ID))) {return Mono.empty();}

//        Bot Check
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
        }
        return Mono.empty();
    }
}