package com.thousand_uncles.discord_bot;

import com.thousand_uncles.discord_bot.services.BotActionsService;
import com.thousand_uncles.discord_bot.util.RecognizedMapsChecker;
import discord4j.core.DiscordClientBuilder;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.lifecycle.ReadyEvent;
import discord4j.gateway.intent.Intent;
import discord4j.gateway.intent.IntentSet;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.jdbc.autoconfigure.DataSourceAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import reactor.util.Logger;
import reactor.util.Loggers;

import java.util.List;

@SpringBootApplication(
        exclude = {DataSourceAutoConfiguration.class},
        scanBasePackages = {
                "com.thousand_uncles.discord_bot"
        })
public class DiscordBotApplication {
    private static final String token = System.getProperty("BOT_TOKEN");
    private static final Logger log = Loggers.getLogger(DiscordBotApplication.class);

	public static void main(String[] args) {

        IO.println("Setting up..");
        ApplicationContext context = new SpringApplicationBuilder(DiscordBotApplication.class)
				.build()
				.run(args);
        List<String> missingMaps = RecognizedMapsChecker.checkMapListValidity();
        assert missingMaps != null;
        if (!missingMaps.isEmpty()) {
            StringBuilder complaint = new StringBuilder("Tell pumpum that");
            for (String map : missingMaps) {
                complaint.append("\n").append(map);
            }
            complaint.append("\nare missing and are not tracked");
            BotActionsService botActionsService = context.getBean(BotActionsService.class);
            botActionsService.sendIntoCave(String.valueOf(complaint));
        }
        IO.println("Set up.");
	}

    @SuppressWarnings("unused")
    @Bean
    public GatewayDiscordClient gatewayDiscordClient() {
        IO.println("token: " + token);

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
}
