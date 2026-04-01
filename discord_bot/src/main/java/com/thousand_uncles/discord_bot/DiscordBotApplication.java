package com.thousand_uncles.discord_bot;

import discord4j.core.DiscordClientBuilder;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.object.presence.ClientActivity;
import discord4j.core.object.presence.ClientPresence;
import discord4j.rest.RestClient;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class DiscordBotApplication {

	public static void main(String[] args) {

        System.out.println("before builder");
		new SpringApplicationBuilder(DiscordBotApplication.class)
				.build()
				.run(args);
        System.out.println("after builder");
	}

    @Bean
    public GatewayDiscordClient gatewayDiscordClient() {
        System.out.println("got to gatewaydiscordClient");
        System.out.println("token: " + System.getProperty("BOT_TOKEN"));
        return DiscordClientBuilder.create(System.getProperty("BOT_TOKEN")).build()
                .gateway()
                .setInitialPresence(ignore -> ClientPresence.online(ClientActivity.listening("silly thoughts:p")))
                .login()
                .block();

    }

    @Bean
    public RestClient discordRestClient(GatewayDiscordClient client) {
        System.out.println("got to getRestClient");
        return client.getRestClient();
    }
}
