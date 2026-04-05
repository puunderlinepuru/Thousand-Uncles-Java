package com.thousand_uncles.discord_bot.commands;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.object.command.ApplicationCommandInteractionOption;
import discord4j.core.object.command.ApplicationCommandInteractionOptionValue;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.io.File;


@Component
public class CheckCommand implements SlashCommand {
    @Override
    public String getName() {
        return "check";
    }


//    TODO use subcommand groups for map types and subcommands for maps
//    TODO find ANY info on how to do subcommands in Discord4J
    @Override
    public Mono<Void> handle(ChatInputInteractionEvent event){
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(new File("resources/records.json"));

            String map = event.getOption("map")
                    .flatMap(ApplicationCommandInteractionOption::getValue)
                    .map(ApplicationCommandInteractionOptionValue::asString)
                    .get();
//            System.out.println("jsonNode: \n" + jsonNode);
//            System.out.println("test: " + jsonNode.get("cp_dustbowl"));
            JsonNode mapNode = jsonNode.get(map);

            System.out.println("map_node: " + mapNode);
            System.out.println("getting: "+ map);
            String response = "";
            response += "Current WR: " + mapNode.get("curr_time").asText() + "\n";
            response += "Previous WR: " + mapNode.get("prev_time").asText() + "\n";
            if (mapNode.get("image_proof2_link") == null) {
                response += "Proof picture: " + mapNode.get("image_proof1_link").asText() + "\n";
            } else {
                response += "Proof picture 1: " + mapNode.get("image_proof1_link").asText() + "\n";
                response += "Proof picture 2: " + mapNode.get("image_proof2_link").asText() + "\n";
                response += "Proof picture 3: " + mapNode.get("image_proof3_link").asText() + "\n";
            }

            if (mapNode.get("video_proof_link") != null) {
                response+= "Video picture: " + mapNode.get("video_proof_link").asText();
            }

            return event.reply()
                    .withEphemeral(true)
                    .withContent(response);
        } catch (Exception e) {
            System.out.println("[ERROR]" + e);
            return event.reply()
                    .withEphemeral(true)
                    .withContent("Beep Boop.. There was an error loading the map, sorry :c");
        }
    }
}
