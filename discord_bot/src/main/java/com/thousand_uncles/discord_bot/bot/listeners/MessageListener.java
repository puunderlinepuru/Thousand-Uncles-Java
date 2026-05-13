package com.thousand_uncles.discord_bot.bot.listeners;

import com.thousand_uncles.discord_bot.bot.YamlReader;
import com.thousand_uncles.discord_bot.bot.fun_stuff.Magic_8_ball;
import com.thousand_uncles.discord_bot.bot.fun_stuff.RandomDictionary;
import com.thousand_uncles.discord_bot.bot.fun_stuff.RandomNumberInRange;
import discord4j.common.util.Snowflake;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.emoji.Emoji;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Component
@SuppressWarnings("unused")
public class MessageListener {
    static YamlReader configReader = new YamlReader("resources/config.yml");
    static Map config = configReader.yamlRead();
    private static final String SERVER_ID = (String) config.get("server_id");
    private static final String MEME_CHANNEL_ID = (String) config.get("meme_channel_id");
    private static final String CURRENTLY_GAMING_CHANNEL_ID = (String) config.get("currently_gaming_channel_id");
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


//        @ Timeout
        if (
                message.getChannelId().equals(Snowflake.of(CURRENTLY_GAMING_CHANNEL_ID))
                && !message.getUserMentions().isEmpty()
                && !message.getType().equals(Message.Type.REPLY)
        ) {
            System.out.println("[ MESSAGE ] at currently-gaming-channel");

//            Structure @rock ... mute @person ...
            if (
                    message.getUserMentionIds().contains(client.getSelfId()) &&
                            message.getUserMentionIds().size() > 1 &&
                            message.getContent().contains("mute <@")
            ) {

                String messageContent = message.getContent();


                int indexOfMute = messageContent.indexOf(" mute <@");
                int identifierEndIndex = messageContent.indexOf(">", indexOfMute + 7);
                if (identifierEndIndex == -1) {
                    return Mono.empty();
                }

                String badMemberID = messageContent.substring(indexOfMute+8, identifierEndIndex);

                Guild guild = message.getGuild().block();

                assert guild != null;
                try {
                    Member badMember = guild.getMemberById(Snowflake.of(badMemberID)).block();

                    assert badMember != null;
                    timeoutMember(badMember, Duration.ofSeconds(30));

                    String responseContent = "timed out " + badMember.getDisplayName() + " :3";
                    return message.getChannel()
                            .flatMap(channel -> channel.createMessage(responseContent))
                            .then();
                } catch (Exception e) {
                    System.out.println("[ ERROR ]" + e.getMessage());

                    return Mono.empty();
                }



            }
        }

//        Downvote
        if (message.getAuthor().get().getId().equals(Snowflake.of("229734102071246850"))){
            message.addReaction(Emoji.of(Long.parseLong("1393275379623399465"),"thumbsdown", false)).block();
            System.out.println("matched");
        }

//        Bot Check
        if ( message.getChannelId().equals(Snowflake.of(MEME_CHANNEL_ID)) ) {
            System.out.println("[ MESSAGE ] at meme-channel");
            System.out.println("mentions: \n" +message.getUserMentionIds().contains(client.getSelfId()));
            if (!message.getUserMentionIds().contains(client.getSelfId())) {return Mono.empty();}
            String response;
            String messageContent = message.getContent();

            Pattern rangePattern = Pattern.compile("number between\\s+(\\d+)\\s+and\\s+(\\d+)");
            Matcher rangeMatcher = rangePattern.matcher(messageContent);

            if (messageContent.contains("?"))
            {
                response = Magic_8_ball.getAnswers();
            } else if (rangeMatcher.find()) {
                int min = Integer.parseInt(rangeMatcher.group(1));
                int max = Integer.parseInt(rangeMatcher.group(2));
                if (min >= max) {
                    response = "Invalid range";
                } else {
                    response = String.valueOf(RandomNumberInRange.getNumber(min, max));
                }
            } else {
                response = RandomDictionary.getWisdom();
            }
            String finalResponse = response;
            return message.getChannel()
                    .flatMap(channel -> channel.createMessage(finalResponse))
                    .then();
        }
        return Mono.empty();
    }
}