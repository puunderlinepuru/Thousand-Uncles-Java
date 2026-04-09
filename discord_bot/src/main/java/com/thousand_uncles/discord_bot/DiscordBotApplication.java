package com.thousand_uncles.discord_bot;

import discord4j.common.util.Snowflake;
import discord4j.core.DiscordClient;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.lifecycle.ReadyEvent;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.channel.TextChannel;
import discord4j.core.object.presence.ClientActivity;
import discord4j.core.object.presence.ClientPresence;
import discord4j.rest.RestClient;
import jakarta.annotation.PostConstruct;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import reactor.core.publisher.Mono;
import reactor.util.Logger;
import reactor.util.Loggers;

import java.util.Map;

@SpringBootApplication
public class DiscordBotApplication {

    private static final String token = System.getProperty("BOT_TOKEN");

    static YamlReader configReader = new YamlReader("resources/config.yml");
    static Map config = configReader.yamlRead();
    private static final String SERVER_ID = (String) config.get("server_id");
    private static final String MEME_CHANNEL_ID = (String) config.get("meme_channel_id");
    private static final String READY_MESSAGE = (String) config.get("ready_message");

    private static final Logger log = Loggers.getLogger(DiscordBotApplication.class);

	public static void main(String[] args) {

        System.out.println("Setting up..");
		new SpringApplicationBuilder(DiscordBotApplication.class)
				.build()
				.run(args);
        System.out.println("Set up.");
	}

    @PostConstruct
    public void discordClient() {

        if (READY_MESSAGE.isEmpty()){
            DiscordClient.create(token)
                    .gateway()
                    .setInitialPresence(s -> ClientPresence.online(
                            ClientActivity.playing("with a new feature").withState("and an extra state")))
                    .withGateway(client -> client.on(ReadyEvent.class)
                            .doOnNext(ready -> log.info("Logged in as {}", ready.getSelf().getUsername()))
                            .then())
                    .block();
        } else {
            DiscordClient.create(token)
                    .gateway()
                    .setInitialPresence(s -> ClientPresence.online(
                            ClientActivity.playing("with a new feature").withState("and an extra state")))
                    .withGateway(gw -> {
                        Mono<Message> sendMessage = gw.on(ReadyEvent.class)
                                .flatMap(e -> e.getClient().getGuildById(Snowflake.of(SERVER_ID)))
                                .next()
                                .flatMap(e -> e.getChannelById(Snowflake.of(MEME_CHANNEL_ID)))
                                .ofType(TextChannel.class)
                                .flatMap(channel -> channel.createMessage(READY_MESSAGE));
                        return sendMessage
                                .then();
                    })
                    .block();
        }


    }

    @Bean
    public RestClient discordRestClient(GatewayDiscordClient client) {
        return client.getRestClient();
    }
}
