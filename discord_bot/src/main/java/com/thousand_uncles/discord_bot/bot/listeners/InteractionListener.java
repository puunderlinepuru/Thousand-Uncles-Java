package com.thousand_uncles.discord_bot.bot.listeners;

import com.thousand_uncles.discord_bot.bot.util.*;
import com.thousand_uncles.discord_bot.data.models.MapRecord;
import com.thousand_uncles.discord_bot.data.service.MapRecordService;
import discord4j.common.util.Snowflake;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.Event;
import discord4j.core.event.domain.interaction.ButtonInteractionEvent;
import discord4j.core.event.domain.interaction.SelectMenuInteractionEvent;
import discord4j.core.event.domain.message.ReactionAddEvent;
import discord4j.core.object.component.ActionRow;
import discord4j.core.object.component.SelectMenu;
import discord4j.core.object.entity.Member;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.Role;
import discord4j.core.object.reaction.Reaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import java.util.List;

@SuppressWarnings("unused")
@Component
public class InteractionListener {

    @Autowired
    ApplicationContext applicationContext;

    @Autowired
    Config config;

    GatewayDiscordClient client;

    String REGION_ROLE_MESSAGE_ID;
    String SERVER_ID;
    String NA_ROLE_ID;
    String EU_ROLE_ID;
    String AU_ROLE_ID;
    String ASIA_ROLE_ID;

    public InteractionListener(GatewayDiscordClient client, Config config) {
        this.client = client;
        System.out.println("InteractionListener initialized");
        REGION_ROLE_MESSAGE_ID = config.getRegion_role_message_id();
        SERVER_ID = config.getServer_id();
        NA_ROLE_ID = config.getNa_role_id();
        EU_ROLE_ID = config.getEu_role_id();
        AU_ROLE_ID = config.getAu_role_id();
        ASIA_ROLE_ID = config.getAsia_role_id();

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
        MapRecordService mapRecordService = applicationContext.getBean(MapRecordService.class);

        System.out.println("select menu");
        String selectedOption = event.getValues().getFirst();
        String customID = event.getCustomId();

        if (customID.startsWith("check") || customID.startsWith("update")){
            String[] parts = customID.split("-");
            String command = parts[0];
            String mapName = parts[1];
            int mapID = GlobalThings.getMapIDS().indexOf(mapName);

            AppNotifications.DISCORD_INTERACTION_INFO(" looking for " + selectedOption + "% category for " + mapName);


            MapRecord gotMap;
            try{
                gotMap = mapRecordService.getRecord(mapID, selectedOption);
            } catch (Exception e) {
                return mapNotFoundResponse(event);
            }
            return foundMapResponse(event, gotMap, selectedOption);
        }

//        Looking for map time
        MapRecord gotMap = mapRecordService.getRecord(GlobalThings.getMapIDS().indexOf(selectedOption), customID);
        System.out.println("found map " + gotMap.getMap_name());

        assert event.getMessage().isPresent();
        System.out.println(event.getMessage().get().getChannel().block());
        event.edit().withContent(BotResponseFormatter.mapRecordToMessageContent(gotMap)).withComponents().block();

        return Mono.empty();
    }

    public Mono<Void> onReaction(ReactionAddEvent event){
        Message message = event.getMessage().block();
        assert message != null;
        for (Reaction reaction : message.getReactions()) {
            if (reaction.getCount() >= 5) {
                System.out.println("funny");
//                Laugh
//                message.addReaction(Emoji.of(Long.valueOf("1312119388576419901"), "name", false));
                message.addReaction(reaction.getEmoji()).block();
            }
        }

        return  Mono.empty();
    }

    private Mono<Void> foundMapResponse(SelectMenuInteractionEvent event, MapRecord foundMap, String recordCategory){
        AppNotifications.DISCORD_INTERACTION_INFO("Found Map");
        List<String> availableToCheckCategories = config.getAvailable_categories().getCheck();
        List<SelectMenu.Option> availableCategoriesOptions = new java.util.ArrayList<>(List.of());
        for (int i = 0; i < availableToCheckCategories.size(); i++) {
            availableCategoriesOptions.add(i, SelectMenu.Option.of(availableToCheckCategories.get(i), availableToCheckCategories.get(i)));
        }
        return event.edit()
                .withEphemeral(true)
                .withContent("Record for " + foundMap.getMap_name() + " - " + recordCategory + ":\n" +
                        BotResponseFormatter.mapRecordToMessageContent(foundMap) + "\n " +
                        "Other categories:")
                .withComponents(ActionRow.of(
                        SelectMenu.of("check-" + foundMap.getMap_name(), availableCategoriesOptions)
                                .withMaxValues(1)));
    }

    private Mono<Void> mapNotFoundResponse(SelectMenuInteractionEvent event){
        return event.reply()
                .withEphemeral(true)
                .withContent("Beep Boop.. This record doesn't exist, try again :p");
    }

}
