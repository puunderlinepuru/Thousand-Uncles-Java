package com.thousand_uncles.discord_bot.bot.commands;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thousand_uncles.discord_bot.data.models.MapRecord;
import com.thousand_uncles.discord_bot.data.service.MapRecordService;
import com.thousand_uncles.discord_bot.data.util.RecordFormatter;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.object.command.ApplicationCommandInteractionOption;
import discord4j.core.object.command.ApplicationCommandInteractionOptionValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.io.File;

@SuppressWarnings("unused")
@Component
public class CheckCommand implements SlashCommand {

    @SuppressWarnings("unassigned")
    @Autowired
    private ApplicationContext applicationContext;


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

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(new File("shared/records.json"));

            @SuppressWarnings("")
            String map = event.getOption("map")
                    .flatMap(ApplicationCommandInteractionOption::getValue)
                    .map(ApplicationCommandInteractionOptionValue::asString)
                    .orElse(null);

            JsonNode mapNode = jsonNode.get(map);

            MapRecord requestedMap = mapRecordService.getRecordByName(map);
            assert requestedMap != null : "[ DATABASE ERROR ] Failed to fetch map";

            System.out.println("name: " + requestedMap.getMap_name());
            System.out.println("current WR: " + requestedMap.getCurr_wr_seconds());
            System.out.println("previous WR: " + requestedMap.getPrev_wr_seconds());
            System.out.println("Proof 1: " + requestedMap.getProof_img_1_link());
            System.out.println("Proof 2: " + requestedMap.getProof_img_2_link());
            System.out.println("Proof 3: " + requestedMap.getProof_img_3_link());
            System.out.println("Proof Video: " + requestedMap.getProof_vid_link());
            System.out.println("Stage 1 time: " + requestedMap.getStage_1_time_seconds());
            System.out.println("Stage 2 time: " + requestedMap.getStage_2_time_seconds());
            System.out.println("Stage 3 time: " + requestedMap.getStage_3_time_seconds());


            String response = getResponse(requestedMap);

            return event.reply()
                    .withEphemeral(true)
                    .withContent(response);
        } catch (Exception e) {
            System.out.println("[ ERROR ] " + e);
            return event.reply()
                    .withEphemeral(true)
                    .withContent("Beep Boop.. There was an error loading the map, sorry :c");
        }
    }

    private static String getResponse(MapRecord requestedMap) {
        String response = "";
        response += "Current WR: " + RecordFormatter.NumberToString(requestedMap.getCurr_wr_seconds()) + "\n";
        response += "Previous WR: " + RecordFormatter.NumberToString(requestedMap.getPrev_wr_seconds()) + "\n";
        if (requestedMap.getProof_img_2_link() == null) {
            response += "Proof picture: " + requestedMap.getProof_img_1_link() + "\n";
        } else {
            response += "Proof picture 1: " + requestedMap.getProof_img_1_link() + "\n";
            response += "Proof picture 2: " + requestedMap.getProof_img_2_link() + "\n";
            response += "Proof picture 3: " + requestedMap.getProof_img_3_link() + "\n";
        }

        if (requestedMap.getProof_vid_link() != null) {
            response+= "Video picture: " + requestedMap.getProof_vid_link();
        }
        return response;
    }
}
