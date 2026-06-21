package com.thousand_uncles.discord_bot.bot.commands;

import com.thousand_uncles.discord_bot.bot.util.AppNotifications;
import com.thousand_uncles.discord_bot.bot.util.BotResponseFormatter;
import com.thousand_uncles.discord_bot.bot.util.Config;
import com.thousand_uncles.discord_bot.data.models.MapRecord;
import com.thousand_uncles.discord_bot.data.service.MapRecordServiceProd;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.event.domain.interaction.SelectMenuInteractionEvent;
import discord4j.core.object.command.ApplicationCommandInteractionOption;
import discord4j.core.object.command.ApplicationCommandInteractionOptionValue;
import discord4j.core.object.component.ActionRow;
import discord4j.core.object.component.SelectMenu;
import discord4j.core.object.entity.Message;
import discord4j.core.spec.InteractionApplicationCommandCallbackReplyMono;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import java.util.List;

@SuppressWarnings("unused")
@Component
@Profile("prod")
public class CheckCommand implements SlashCommand {

    @SuppressWarnings("unassigned")
    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private GatewayDiscordClient client;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private Config config;

    @Override
    public String getName() {
        return "check";
    }

    @Override
    public Mono<Void> handle(ChatInputInteractionEvent event){
        try {
            MapRecordServiceProd mapRecordServiceProd = applicationContext.getBean(MapRecordServiceProd.class);

            String partialMapName = event.getOption("map")
                    .flatMap(ApplicationCommandInteractionOption::getValue)
                    .map(ApplicationCommandInteractionOptionValue::asString)
                    .orElse(null);

            System.out.println("gotName: " + partialMapName);

            String category = event.getOption("category")
                    .flatMap(ApplicationCommandInteractionOption::getValue)
                    .map(ApplicationCommandInteractionOptionValue::asString)
                    .orElse("any");


            List<MapRecord> searchedMaps = mapRecordServiceProd.searchRecords(partialMapName, category);
            System.out.println("found maps: " + searchedMaps);
            if (searchedMaps.isEmpty()){
                System.out.println(" no maps found");
                return mapNotFound(event);
            }

            SelectMenu foundMapsMenu;
            List<SelectMenu.Option> foundMapOptions = new java.util.ArrayList<>(List.of());
            if (searchedMaps.size() == 1){
                return singleMapFoundResponse(event, searchedMaps.getFirst(), category);
            }

            for (int i = 0; i < searchedMaps.size(); i++) {
                foundMapOptions.add(i, SelectMenu.Option.of(searchedMaps.get(i).getMap_name(),searchedMaps.get(i).getMap_name()));
            }

            return multipleMapsFoundResponse(event, partialMapName, category, foundMapOptions);


        } catch (Exception e) {
            System.out.println("[ ERROR ] " + e);
            return mapNotFound(event);
        }
    }

    private Mono<Void> multipleMapsFoundResponse(ChatInputInteractionEvent event, String partialMapName, String category, List<SelectMenu.Option> foundMapOptions){
        InteractionApplicationCommandCallbackReplyMono sendMessage = event
                .reply()
                .withContent("Found candidates for " + partialMapName + " - " + category + "%:")
                .withEphemeral(true)
                .withComponents(ActionRow.of(
                        SelectMenu.of("foundMapSelectMenu", foundMapOptions)
                                .withMaxValues(1)));

        return sendMessage
                .flatMapMany(selectMenuMessageId ->
                        client.on(SelectMenuInteractionEvent.class, interactionEvent ->
                                Mono.justOrEmpty(interactionEvent.getInteraction().getMessage())
                                        .map(Message::getId)
                                        .filter(selectMenuMessageId::equals)
                                        .then(interactionEvent.reply(interactionEvent.getValues().toString()))
                        )
                )
                .then();
    }

    private Mono<Void> singleMapFoundResponse(ChatInputInteractionEvent event, MapRecord foundMap, String category){
        AppNotifications.DISCORD_INTERACTION_INFO("Single Map Found Response Case");
        List<String> availableToCheckCategories = config.getAvailable_categories().getCheck();
        List<SelectMenu.Option> availableCategoriesOptions = new java.util.ArrayList<>(List.of());
        for (int i = 0; i < availableToCheckCategories.size(); i++) {
            availableCategoriesOptions.add(i, SelectMenu.Option.of(availableToCheckCategories.get(i), availableToCheckCategories.get(i)));
        }

        return event.reply()
                .withEphemeral(true)
                .withContent("Record for " + foundMap.getMap_name() + " - " + "%" + ":\n" +
                        BotResponseFormatter.mapRecordToMessageContent(foundMap) + "\n " +
                        "Other categories:")
                .withComponents(ActionRow.of(
                        SelectMenu.of("check-" + foundMap.getMap_name(), availableCategoriesOptions)
                                .withMaxValues(1)));
    }

    private Mono<Void> mapNotFound(ChatInputInteractionEvent event){
        return event.reply()
                .withEphemeral(true)
                .withContent("Beep Boop.. There was an error loading the map, sorry :c");
    }
}
