package com.thousand_uncles.discord_bot;

import discord4j.core.DiscordClientBuilder;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.lifecycle.ReadyEvent;
import discord4j.gateway.intent.Intent;
import discord4j.gateway.intent.IntentSet;
import discord4j.rest.RestClient;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import reactor.util.Logger;
import reactor.util.Loggers;

@SpringBootApplication
public class DiscordBotApplication {

    private static final String token = System.getProperty("BOT_TOKEN");
    private static final Logger log = Loggers.getLogger(DiscordBotApplication.class);

	public static void main(String[] args) {

        System.out.println("Setting up..");
        new SpringApplicationBuilder(DiscordBotApplication.class)
				.build()
				.run(args);
        System.out.println("Set up.");

	}

    @SuppressWarnings("unused")
    @Bean
    public GatewayDiscordClient gatewayDiscordClient() {
        System.out.println("got to gatewaydiscordClient");
        System.out.println("token: " + token);

         GatewayDiscordClient client = DiscordClientBuilder.create(token).build()
                .gateway()
                .setEnabledIntents(IntentSet.of(
                        Intent.GUILDS,
                        Intent.GUILD_MEMBERS,
                        Intent.GUILD_MESSAGES,
                        Intent.MESSAGE_CONTENT,
                        Intent.GUILD_MESSAGE_REACTIONS
                ))
                .login()
                .block();

        assert client != null;
        client.on(ReadyEvent.class)
                .doOnNext(ready -> log.info("Logged in as {}", ready.getSelf().getUsername()))
                .then();

        return client;
    }

    @SuppressWarnings("unused")
    @Bean
    public RestClient discordRestClient(GatewayDiscordClient client) {
        return client.getRestClient();
    }
}
