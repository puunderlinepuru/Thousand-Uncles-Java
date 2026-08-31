package com.thousand_uncles.discord_bot.bot.listeners;

import com.thousand_uncles.data.models.common.ManualIndexedMapRecordEntry;
import com.thousand_uncles.data.service.MapRecordServiceProd;
import com.thousand_uncles.discord_bot.common.util.AppNotifications;
import com.thousand_uncles.discord_bot.bot.util.DiscordBotResponseFormatter;
import com.thousand_uncles.discord_bot.common.config.BotConfig;
import com.thousand_uncles.discord_bot.common.util.GlobalThings;
import discord4j.common.util.Snowflake;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.Event;
import discord4j.core.event.domain.interaction.ButtonInteractionEvent;
import discord4j.core.event.domain.interaction.SelectMenuInteractionEvent;
import discord4j.core.event.domain.message.ReactionAddEvent;
import discord4j.core.object.component.ActionRow;
import discord4j.core.object.component.Button;
import discord4j.core.object.component.SelectMenu;
import discord4j.core.object.entity.Member;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.Role;
import discord4j.core.object.reaction.Reaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import java.util.ArrayList;
import java.util.List;

// I HAVEN'T UPDATED IT IN A WHILE

@SuppressWarnings("unused")
@Profile("dev")
@Component
public class InteractionListenerDev {

    @Autowired
    ApplicationContext applicationContext;

    @Autowired
    BotConfig botConfig;

    GatewayDiscordClient client;

    String REGION_ROLE_MESSAGE_ID;
    String ADMI_ROLE_ID;
    String SERVER_ID;
    String NA_ROLE_ID;
    String EU_ROLE_ID;
    String AU_ROLE_ID;
    String ASIA_ROLE_ID;

    public InteractionListenerDev(GatewayDiscordClient client, BotConfig botConfig) {
        this.client = client;
        System.out.println("InteractionListener initialized");
        REGION_ROLE_MESSAGE_ID = botConfig.getRegion_role_message_id();
        ADMI_ROLE_ID = botConfig.getAdmi_role_id();
        SERVER_ID = botConfig.getServer_id();
        NA_ROLE_ID = botConfig.getNa_role_id();
        EU_ROLE_ID = botConfig.getEu_role_id();
        AU_ROLE_ID = botConfig.getAu_role_id();
        ASIA_ROLE_ID = botConfig.getAsia_role_id();

        client.on(ButtonInteractionEvent.class, this::onButton).subscribe();

        client.on(SelectMenuInteractionEvent.class, this::onSelectMenu).subscribe();

        client.on(ReactionAddEvent.class, this::onReaction).subscribe();

        client.on(Event.class, this::generalEvent).subscribe();
    }


    public Mono<Void> generalEvent(Event event){
        System.out.println("Event type: " + event.getClass());

        return Mono.empty();
    }

    public Mono<Void> onButton(ButtonInteractionEvent event){
        String customID = event.getCustomId();
        System.out.println("button: " + customID);

        if (customID.startsWith("approve-")){
            if (event.getMessage().isEmpty()){ return Mono.empty();}

            Member whoClicked = event.getUser().asMember(Snowflake.of(SERVER_ID)).block();

            assert whoClicked != null;
            if (!whoClicked.getRoleIds().contains(Snowflake.of(ADMI_ROLE_ID))){
                return Mono.empty();
            }

            event.reply()
                    .withEphemeral(true)
                    .withContent("I am running in offline mode, as pu when I'll be online so I can verify the record :p")
                    .block();
        }
//        Region role assignment
        if (event.getMessageId().equals(Snowflake.of(REGION_ROLE_MESSAGE_ID))){
            return assignRegionRole(event, customID);
        }

        if (customID.equals("petButton")){
            return petHandle(event);
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

            AppNotifications.Discord.DISCORD_INTERACTION_INFO(" looking for " + selectedOption + "% category for " + mapName);


            ManualIndexedMapRecordEntry gotMap;
            try{
                gotMap = mapRecordServiceProd.getRecord(mapID, selectedOption);
            } catch (Exception e) {
                return mapNotFoundResponse(event);
            }
            return foundMapResponse(event, gotMap, selectedOption);
        }

//        Looking for map time
        ManualIndexedMapRecordEntry gotMap = mapRecordServiceProd.getRecord(GlobalThings.getMapIDS().indexOf(selectedOption), customID);
        System.out.println("found map " + gotMap.getMap_name());

        assert event.getMessage().isPresent();
        System.out.println(event.getMessage().get().getChannel().block());
        event.edit().withContent(DiscordBotResponseFormatter.mapRecordToMessageContent(gotMap)).withComponents().block();

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

    private Mono<Void> assignRegionRole(ButtonInteractionEvent event, String customID){
        Member member = event.getUser().asMember(Snowflake.of(SERVER_ID)).block();

        assert member != null;
        List<Snowflake> memberRoleIDs = member.getRoles()
                .map(Role::getId)
                .collectList()
                .block();
        assert memberRoleIDs != null;

        switch (customID){
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

    private Mono<Void> petHandle(ButtonInteractionEvent event){

        System.out.println("a");
        int pets = GlobalThings.getPets();
        pets++;
        System.out.println("pets: " + pets);
        if (pets >= 10){
            GlobalThings.setAppLocked(false);
            GlobalThings.setPets(0);
            return event.getMessage().get()
                    .edit()
                    .withContentOrNull("Good.")
                    .withComponents()
                    .then(event.deferEdit());
        } else {
            GlobalThings.setPets(pets);
            return event.getMessage().get()
                    .edit()
                    .withContentOrNull("You have hit the rock tax. Pet me meow \n" +
                            "pet pet pet c:<\n" +
                            "pets: " + pets)
                    .withComponents(ActionRow.of(
                            Button.primary("petButton", "Pet")))
                    .then(event.deferEdit());
        }
    }

    private Mono<Void> foundMapResponse(SelectMenuInteractionEvent event, ManualIndexedMapRecordEntry foundMap, String recordCategory){
        AppNotifications.Discord.DISCORD_INTERACTION_INFO("Found Map");
        List<String> availableToCheckCategories = botConfig.getAvailable_categories().getCheck();
        List<SelectMenu.Option> availableCategoriesOptions = new ArrayList<>(List.of());
        for (int i = 0; i < availableToCheckCategories.size(); i++) {
            availableCategoriesOptions.add(i, SelectMenu.Option.of(availableToCheckCategories.get(i), availableToCheckCategories.get(i)));
        }
        return event.edit()
                .withEphemeral(true)
                .withContent("Record for " + foundMap.getMap_name() + " - " + recordCategory + ":\n" +
                        DiscordBotResponseFormatter.mapRecordToMessageContent(foundMap) + "\n " +
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
