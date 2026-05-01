package com.thousand_uncles.discord_bot.bot.listeners;

import com.thousand_uncles.discord_bot.bot.YamlReader;
import discord4j.common.util.Snowflake;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.guild.MemberJoinEvent;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Map;

@Component
public class JoinListener {
    static YamlReader configReader = new YamlReader("resources/config.yml");
    static Map config = configReader.yamlRead();
    private static final String ROLE_ON_JOIN_ID = (String) config.get("role_on_join_id");

    GatewayDiscordClient client;

    public JoinListener(GatewayDiscordClient client) {

        client.on(MemberJoinEvent.class, this::onJoin).subscribe();
    }

    public Mono<Void> onJoin(MemberJoinEvent event) {

        System.out.println("Somebody joined! Assigning a role..");
        event.getMember().addRole(Snowflake.of(ROLE_ON_JOIN_ID));
        return Mono.empty();
    }
}
