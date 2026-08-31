package com.thousand_uncles.discord_bot.bot.listeners;

import com.thousand_uncles.discord_bot.bot.fun_stuff.Magic_8_ball;
import com.thousand_uncles.discord_bot.bot.fun_stuff.RandomDictionary;
import com.thousand_uncles.discord_bot.bot.fun_stuff.RandomNumberInRange;
import com.thousand_uncles.discord_bot.common.config.BotConfig;
import com.thousand_uncles.discord_bot.common.util.GlobalThings;
import discord4j.common.util.Snowflake;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.component.ActionRow;
import discord4j.core.object.component.Button;
import discord4j.core.object.entity.Guild;
import discord4j.core.object.entity.Member;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.User;
import discord4j.core.spec.GuildMemberEditSpec;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import java.time.Duration;
import java.time.Instant;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Component
@SuppressWarnings("unused")
public class MessageListener {
    private final String THE_CAVE;
    private final String CURRENTLY_GAMING_CHANNEL_ID;

    GatewayDiscordClient client;

    public MessageListener(GatewayDiscordClient client, BotConfig botConfig) {
        THE_CAVE = botConfig.getThe_cave_channel_id();
        CURRENTLY_GAMING_CHANNEL_ID = botConfig.getCurrently_gaming_channel_id();

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
        System.out.println(event.toString());
        final Message message = event.getMessage();
//        System.out.println("Data: " + message.getData());
//        System.out.println("Poll: " + message.getPoll());
//        System.out.println("embeds: " + message.getEmbeds());
        /*[Embed{
            data=EmbedData{
                title=Possible.absent,
                type=Possible{poll_result},
                description=Possible.absent,
                url=Possible.absent,
                timestamp=Possible.absent,
                color=Possible.absent,
                footer=Possible.absent,
                image=Possible.absent,
                thumbnail=Possible.absent,
                video=Possible.absent,
                provider=Possible.absent,
                author=Possible.absent,
                fields=[
                    EmbedFieldData{
                        name=poll_question_text,
                        value=poll,
                        inline=Possible{false}
                    },
                    EmbedFieldData{
                        name=victor_answer_votes,
                        value=1,
                        inline=Possible{false}
                    },
                    EmbedFieldData{
                        name=total_votes,
                        value=1,
                        inline=Possible{false}
                    },
                    EmbedFieldData{
                        name=victor_answer_id,
                        value=2,
                        inline=Possible{false}
                    },
                    EmbedFieldData{
                        name=victor_answer_text,
                        value=2,
                        inline=Possible{false}
                    }
                ]
            }
        }
        ]*/
//        TODO Make custom polls?
        final String messageContent = message.getContent();

        if(GlobalThings.isAppLocked()){
            return Mono.empty();
        }

        //        Bot Check
        if (message.getAuthor().map(User::isBot).orElse(false)) {return Mono.empty();}

        if (messageContent.toLowerCase().contains("invincible")){
            return message.getChannel()
                    .flatMap(channel -> channel.createMessage("https://tenor.com/view/invulnerable-gif-22484955"))
                    .then();
        }

//  @rock BLOCK

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
                            messageContent.contains("mute <@")
            ) {
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

//        Utility random things - coinflip & number between
        String response;

//        number between A and B
        Pattern rangePattern = Pattern.compile("number between\\s+(\\d+)\\s+and\\s+(\\d+)");
        Matcher rangeMatcher = rangePattern.matcher(messageContent);
        if (rangeMatcher.find()) {
            int min = Integer.parseInt(rangeMatcher.group(1));
            int max = Integer.parseInt(rangeMatcher.group(2));
            if (min >= max) {
                response = "Invalid range";
            } else {
                response = String.valueOf(RandomNumberInRange.getNumber(min, max));
            }
            return message.getChannel()
                    .flatMap(channel -> channel.createMessage(response))
                    .then();
        }

//        Message only in meme channel
        if ( message.getChannelId().equals(Snowflake.of(THE_CAVE)) ) {
            if (!message.getUserMentionIds().contains(client.getSelfId())) {return Mono.empty();}
            if (messageContent.contains("?"))
            {
                response = Magic_8_ball.getAnswers();
            } else {
                response = RandomDictionary.getWisdom().orElse(null);
                if (response == null){
                    return petMessage(message);
                }
            }
            return message.getChannel()
                    .flatMap(channel -> channel.createMessage(response))
                    .then();
        }
        return Mono.empty();
    }

    private Mono<Void> petMessage(Message message){
        GlobalThings.setAppLocked(true);

        return message.getChannel()
                .flatMap(channel -> channel.createMessage()
                        .withContent("You have hit the rock tax. Pet me meow \n pet pet pet c:<")
                        .withComponents(ActionRow.of(
                                Button.primary("petButton", "Pet"))))
                .then();
    }
}