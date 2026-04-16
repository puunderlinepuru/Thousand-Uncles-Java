package com.thousand_uncles.discord_bot.listeners;

import com.thousand_uncles.discord_bot.YamlReader;
import com.thousand_uncles.discord_bot.fun_stuff.*;
import discord4j.common.util.Snowflake;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Guild;
import discord4j.core.object.entity.Member;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.User;
import discord4j.core.spec.GuildMemberEditSpec;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Map;


@Component
public class MessageListener {
    static YamlReader configReader = new YamlReader("resources/config.yml");
    static Map config = configReader.yamlRead();
    private static final String MEME_CHANNEL_ID = (String) config.get("meme_channel_id");
    private static final String CURRENTLY_GAMING_CHANNEL_ID = (String) config.get("meme_channel_id");
    private static final List<String> USERS_TO_TIMEOUT = (List<String>) config.get("users_to_timeout");

    GatewayDiscordClient client;

    public MessageListener(GatewayDiscordClient client) {

        this.client = client;

        client.on(MessageCreateEvent.class, this::onMessage).subscribe();
    }

    public void timeoutMember(Member member, Duration duration) {
        member.edit(GuildMemberEditSpec.builder().communicationDisabledUntilOrNull(Instant.now().plus(duration)).build())
                .subscribe(
                        updatedMember -> System.out.println("Successfully timed out member: " + updatedMember.getDisplayName()),
                        error -> System.err.println("Failed to timeout member: " + error.getMessage())
                );
    }

    public Mono<Void> onMessage(MessageCreateEvent event) {
        final Message message = event.getMessage();

        if (message.getAuthor().map(User::isBot).orElse(false)) {return Mono.empty();}

        if (
                message.getChannelId().equals(Snowflake.of(CURRENTLY_GAMING_CHANNEL_ID))
                || !message.getUserMentions().isEmpty()
        ) {
            System.out.println("[ MESSAGE ] at meme-channel");
            for (String badUserID : USERS_TO_TIMEOUT) {
                if (message.getUserMentionIds().contains(Snowflake.of(badUserID))) {
                    Guild guild = message.getGuild().block();

                    assert guild != null;
                    Member badMember = guild.getMemberById(Snowflake.of(badUserID)).block();

                    badMember.ban();

                    assert badMember != null;
                    timeoutMember(badMember, Duration.ofSeconds(10));

                    return message.getChannel()
                            .flatMap(channel -> channel.createMessage("timed out " + badMember.getDisplayName() + " :3"))
                            .then();
                }
            }
        }

//        Bot Check
        if ( message.getChannelId().equals(Snowflake.of(MEME_CHANNEL_ID)) ) {
            System.out.println("[ MESSAGE ] at meme-channel");
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