package com.thousand_uncles.discord_bot.bot.commands;

import com.thousand_uncles.discord_bot.bot.util.BotResponseFormatter;
import com.thousand_uncles.discord_bot.data.models.MapRecord;
import com.thousand_uncles.discord_bot.data.service.MapRecordService;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.event.domain.interaction.SelectMenuInteractionEvent;
import discord4j.core.object.command.ApplicationCommandInteractionOption;
import discord4j.core.object.command.ApplicationCommandInteractionOptionValue;
import discord4j.core.object.component.ActionRow;
import discord4j.core.object.component.SelectMenu;
import discord4j.core.object.entity.Message;
import discord4j.core.spec.InteractionApplicationCommandCallbackReplyMono;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;

@SuppressWarnings("unused")
@Component
public class CheckCommand implements SlashCommand {

    @SuppressWarnings("unassigned")
    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private GatewayDiscordClient client;


    @Override
    public String getName() {
        return "check";
    }


//    TODO use subcommand groups for map types and subcommands for maps
//    TODO find ANY info on how to do subcommands in Discord4J
    @Override
    public Mono<Void> handle(ChatInputInteractionEvent event){


        try {
            MapRecordService mapRecordService = applicationContext.getBean(MapRecordService.class);

//            ObjectMapper objectMapper = new ObjectMapper();
//            JsonNode jsonNode = objectMapper.readTree(new File("shared/records.json"));

            @SuppressWarnings("")
            String map = event.getOption("map")
                    .flatMap(ApplicationCommandInteractionOption::getValue)
                    .map(ApplicationCommandInteractionOptionValue::asString)
                    .orElse(null);

//            JsonNode mapNode = jsonNode.get(map);


            List<MapRecord> searchedMaps = mapRecordService.searchRecords(map);
            assert searchedMaps != null : "[ DATABASE ERROR ] Failed to fetch map";

            SelectMenu foundMapsMenu;
            List<SelectMenu.Option> foundMapOptions = new java.util.ArrayList<>(List.of());
            if (searchedMaps.size() == 1){
                return event.reply()
                        .withEphemeral(true)
                        .withContent(BotResponseFormatter.getResponse(searchedMaps.getFirst()));
            }

            for (int i = 0; i < searchedMaps.size(); i++) {
                foundMapOptions.add(i, SelectMenu.Option.of(searchedMaps.get(i).getMap_name(),searchedMaps.get(i).getMap_name()));
            }

            InteractionApplicationCommandCallbackReplyMono sendMessage = event
                    .reply()
                    .withContent("a")
                    .withEphemeral(true)
                    .withComponents(ActionRow.of(
                                    SelectMenu.of("mySelectMenu1", foundMapOptions)
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


        } catch (Exception e) {
            System.out.println("[ ERROR ] " + e);
            return event.reply()
                    .withEphemeral(true)
                    .withContent("Beep Boop.. There was an error loading the map, sorry :c");
        }
    }
}
