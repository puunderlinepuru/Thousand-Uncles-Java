package com.thousand_uncles.discord_bot.bot.listeners;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.thousand_uncles.discord_bot.bot.util.Config;
import com.thousand_uncles.discord_bot.bot.util.*;
import com.thousand_uncles.discord_bot.data.models.AnyPercentMapRecord;
import com.thousand_uncles.discord_bot.data.models.ConfirmWorthyMapRecord;
import com.thousand_uncles.discord_bot.data.models.MapRecord;
import com.thousand_uncles.discord_bot.data.models.SoloMapRecord;
import com.thousand_uncles.discord_bot.data.service.MapRecordServiceProd;
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
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SuppressWarnings("unused")
@Component
public class InteractionListener {

    @Autowired
    ApplicationContext applicationContext;

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Autowired
    MapRecordServiceProd mapRecordServiceProd;

    @Autowired
    Config config;

    GatewayDiscordClient client;

    String REGION_ROLE_MESSAGE_ID;
    String ADMI_ROLE_ID;
    String SERVER_ID;
    String NA_ROLE_ID;
    String EU_ROLE_ID;
    String AU_ROLE_ID;
    String ASIA_ROLE_ID;

    public InteractionListener(GatewayDiscordClient client, Config config) {
        this.client = client;
        System.out.println("InteractionListener initialized");
        REGION_ROLE_MESSAGE_ID = config.getRegion_role_message_id();
        ADMI_ROLE_ID = config.getAdmi_role_id();
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
        String customId = event.getCustomId();
        System.out.println("button: " + customId);

        if (customId.startsWith("approve-")){
            if (event.getMessage().isEmpty()){ return Mono.empty();}

            Member whoClicked = event.getUser().asMember(Snowflake.of(SERVER_ID)).block();

            assert whoClicked != null;
            if (!whoClicked.getRoleIds().contains(Snowflake.of(ADMI_ROLE_ID))){
                return Mono.empty();
            }

            String[] partsOfCustomID = customId.split("-");
            System.out.println("Button press, got parts: " + Arrays.toString(partsOfCustomID));
            String category = partsOfCustomID[1];
            String map = partsOfCustomID[2];
            short mapTime = Short.parseShort(partsOfCustomID[3]);

            ConfirmWorthyMapRecord confirmWorthyMapRecord;
            confirmWorthyMapRecord = mapRecordServiceProd.getFromHold(category, GlobalThings.getMapIDS().indexOf(map));

            MapRecord foundMap = null;

            switch (category){
                case "any":
                    AnyPercentMapRecord anyPercentMapRecord = new AnyPercentMapRecord();
                    anyPercentMapRecord.setId(GlobalThings.getMapIDS().indexOf(map));
                    anyPercentMapRecord.setMap_name(map);
                    anyPercentMapRecord.setCurr_wr_seconds(mapTime);
                    anyPercentMapRecord.setPrev_wr_seconds((short) 0);
                    anyPercentMapRecord.setProof_img_1_link(confirmWorthyMapRecord.getProof_img_1_link());
                    anyPercentMapRecord.setProof_img_2_link(confirmWorthyMapRecord.getProof_img_2_link());
                    anyPercentMapRecord.setProof_img_3_link(confirmWorthyMapRecord.getProof_img_3_link());
                    anyPercentMapRecord.setProof_vid_link(confirmWorthyMapRecord.getProof_vid_link());
                    anyPercentMapRecord.setStage_1_time_seconds(confirmWorthyMapRecord.getStage_1_time_seconds());
                    anyPercentMapRecord.setStage_2_time_seconds(confirmWorthyMapRecord.getStage_2_time_seconds());
                    anyPercentMapRecord.setStage_3_time_seconds(confirmWorthyMapRecord.getStage_3_time_seconds());

                    foundMap = anyPercentMapRecord;
                    break;
                case "solo":
                    String theHero;
                    try {
                        ObjectMapper objectMapper = new ObjectMapper();
                        String theHeroString = confirmWorthyMapRecord.getAdditional();
                        JsonNode theHeroNode = objectMapper.readTree(theHeroString);
                        System.out.println(" Upon transforming json to the hero got: " + theHeroNode);
                        theHero = theHeroNode.get("the_hero").asText();
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }

                    SoloMapRecord soloMapRecord = new SoloMapRecord();
                    soloMapRecord.setId(GlobalThings.getMapIDS().indexOf(map));
                    soloMapRecord.setMap_name(map);
                    soloMapRecord.setCurr_wr_seconds(mapTime);
                    soloMapRecord.setPrev_wr_seconds((short) 0);
                    soloMapRecord.setThe_hero(theHero);
                    soloMapRecord.setProof_img_1_link(confirmWorthyMapRecord.getProof_img_1_link());
                    soloMapRecord.setProof_img_2_link(confirmWorthyMapRecord.getProof_img_2_link());
                    soloMapRecord.setProof_img_3_link(confirmWorthyMapRecord.getProof_img_3_link());
                    soloMapRecord.setProof_vid_link(confirmWorthyMapRecord.getProof_vid_link());
                    soloMapRecord.setStage_1_time_seconds(confirmWorthyMapRecord.getStage_1_time_seconds());
                    soloMapRecord.setStage_2_time_seconds(confirmWorthyMapRecord.getStage_2_time_seconds());
                    soloMapRecord.setStage_3_time_seconds(confirmWorthyMapRecord.getStage_3_time_seconds());

                    foundMap = soloMapRecord;
                    break;
            }

            try{
                ObjectMapper objectMapper = new ObjectMapper();
                ObjectNode objectNode = objectMapper.valueToTree(foundMap);
                objectNode.put("category", category);
                String jsonString = objectMapper.writeValueAsString(objectNode);
                rabbitTemplate.convertAndSend("test.exchange", "test.routing.key", jsonString);
                System.out.println("sent");
            }catch (Exception e) {
                System.out.println("error: " + e);
            }

            Message recordApprovalMessage = event.getMessage().get();
            recordApprovalMessage.edit()
                    .withComponents()
                    .block();
        }

        if (event.getMessageId().equals(Snowflake.of(REGION_ROLE_MESSAGE_ID))){
            Member member = event.getUser().asMember(Snowflake.of(SERVER_ID)).block();

            assert member != null;
            List<Snowflake> memberRoleIDs = member.getRoles()
                    .map(Role::getId)
                    .collectList()
                    .block();
            assert memberRoleIDs != null;

            switch (customId){
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
        MapRecordServiceProd mapRecordServiceProd = applicationContext.getBean(MapRecordServiceProd.class);

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
                gotMap = mapRecordServiceProd.getRecord(mapID, selectedOption);
            } catch (Exception e) {
                return mapNotFoundResponse(event);
            }
            return foundMapResponse(event, gotMap, selectedOption);
        }

//        Looking for map time
        MapRecord gotMap = mapRecordServiceProd.getRecord(GlobalThings.getMapIDS().indexOf(selectedOption), customID);
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
        List<SelectMenu.Option> availableCategoriesOptions = new ArrayList<>(List.of());
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
