package com.thousand_uncles.discord_bot.bot.listeners;

import com.thousand_uncles.discord_bot.bot.util.Config;
import discord4j.common.util.Snowflake;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.guild.MemberJoinEvent;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@SuppressWarnings("unused")
@Component
public class JoinListener {
    private final String ROLE_ON_JOIN_ID;

    GatewayDiscordClient client;

    @SuppressWarnings("unused")
    public JoinListener(GatewayDiscordClient client, Config config) {
        ROLE_ON_JOIN_ID = config.getRole_on_join_id();

        client.on(MemberJoinEvent.class, this::onJoin).subscribe();
    }

    public Mono<Void> onJoin(MemberJoinEvent event) {

        System.out.println("Somebody joined! Assigning a role..");
        event.getMember().addRole(Snowflake.of(ROLE_ON_JOIN_ID)).block();
        return Mono.empty();
    }
}
