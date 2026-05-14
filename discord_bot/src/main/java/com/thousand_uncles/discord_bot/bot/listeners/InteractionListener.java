package com.thousand_uncles.discord_bot.bot.listeners;

import com.thousand_uncles.discord_bot.bot.YamlReader;
import com.thousand_uncles.discord_bot.bot.util.BotResponseFormatter;
import com.thousand_uncles.discord_bot.data.models.MapRecord;
import com.thousand_uncles.discord_bot.data.service.MapRecordService;
import discord4j.common.util.Snowflake;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.Event;
import discord4j.core.event.domain.interaction.ButtonInteractionEvent;
import discord4j.core.event.domain.interaction.SelectMenuInteractionEvent;
import discord4j.core.event.domain.message.ReactionAddEvent;
import discord4j.core.object.entity.Member;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.Role;
import discord4j.core.object.reaction.Reaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@SuppressWarnings("unused")
@Component
public class InteractionListener {
    static YamlReader configReader = new YamlReader("resources/config.yml");
    static Map<String, Object> config = configReader.yamlRead();
    static String REGION_ROLE_MESSAGE_ID = (String) config.get("region_role_message_id");
    static String SERVER_ID = (String) config.get("server_id");
    static String NA_ROLE_ID = (String) config.get("na_role_id");
    static String EU_ROLE_ID = (String) config.get("eu_role_id");
    static String AU_ROLE_ID = (String) config.get("au_role_id");
    static String ASIA_ROLE_ID = (String) config.get("asia_role_id");

    @Autowired
    ApplicationContext applicationContext;

    GatewayDiscordClient client;

    public InteractionListener(GatewayDiscordClient client) {
        this.client = client;

        client.on(ButtonInteractionEvent.class, this::onButton).subscribe();

        client.on(SelectMenuInteractionEvent.class, this::onSelectMenu).subscribe();

        client.on(ReactionAddEvent.class, this::onReaction).subscribe();

        client.on(Event.class, this::generalEvent).subscribe();
    }


    public Mono<Void> generalEvent(Event event){
        System.out.println("Event type: " + event.getClass());

        return Mono.empty();
    }
    public Mono<Void> onButton (ButtonInteractionEvent event){
        System.out.println("button");

        if (event.getMessageId().equals(Snowflake.of(REGION_ROLE_MESSAGE_ID))){
            Member member = event.getUser().asMember(Snowflake.of(SERVER_ID)).block();

            assert member != null;
            List<Snowflake> memberRoleIDs = member.getRoles()
                    .map(Role::getId)
                    .collectList()
                    .block();
            assert memberRoleIDs != null;
            String buttonName = event.getCustomId();

            switch (buttonName){
                case "button_EU":
                    if (memberRoleIDs.contains(Snowflake.of(EU_ROLE_ID))) {
                        member.removeRole(Snowflake.of(EU_ROLE_ID)).block();
                    } else {
                        member.addRole(Snowflake.of(EU_ROLE_ID)).block();
                    }
                    break;
                case "button_NA":
                    if (memberRoleIDs.contains(Snowflake.of(NA_ROLE_ID))) {
                        member.removeRole(Snowflake.of(NA_ROLE_ID)).block();
                    } else {
                        member.addRole(Snowflake.of(NA_ROLE_ID)).block();
                    }
                    break;
                case "button_AU":
                    if (memberRoleIDs.contains(Snowflake.of(AU_ROLE_ID))) {
                        member.removeRole(Snowflake.of(AU_ROLE_ID)).block();
                    } else {
                        member.addRole(Snowflake.of(AU_ROLE_ID)).block();
                    }
                    break;
                case "button_Asia":
                    if (memberRoleIDs.contains(Snowflake.of(ASIA_ROLE_ID))) {
                        member.removeRole(Snowflake.of(ASIA_ROLE_ID)).block();
                    } else {
                        member.addRole(Snowflake.of(ASIA_ROLE_ID)).block();
                    }
                    break;
            }

            return event.deferEdit();
        }

        return Mono.empty();
    }

    public Mono<Void> onSelectMenu (SelectMenuInteractionEvent event){
        System.out.println("select menu");
        System.out.println(event.getValues().getFirst());

        MapRecordService mapRecordService = applicationContext.getBean(MapRecordService.class);
        MapRecord gotMap = mapRecordService.getRecordByName(event.getValues().getFirst());
        System.out.println("found map " + gotMap.getMap_name());

        assert event.getMessage().isPresent();
        System.out.println(event.getMessage().get().getChannel().block());
        event.edit().withContent(BotResponseFormatter.getResponse(gotMap)).withComponents().block();

        return Mono.empty();
    }

    public Mono<Void> onReaction(ReactionAddEvent event){
        Message message = event.getMessage().block();
        assert message != null;
        for (Reaction reaction : message.getReactions()) {
            if (reaction.getCount() >= 5) {
                System.out.println("funny");
            }
        }

        return  Mono.empty();
    }

}
