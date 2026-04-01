package com.thousand_uncles.discord_bot.commands;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.object.command.ApplicationCommandInteractionOption;
import discord4j.core.object.command.ApplicationCommandInteractionOptionValue;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.io.InputStream;

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
        try (
                InputStream recordsResource = CheckCommand.class.getResourceAsStream("/records.json");
//                InputStream recordsResource = new FileInputStream("D:/Test/test.json");
                ) {
            ObjectMapper objectMapper = new ObjectMapper();
//            System.out.println("inputstream read: " + recordsResource.read());
            JsonNode jsonNode = objectMapper.readTree(recordsResource);
            JsonNode map_data = jsonNode.get("map");
            System.out.println("map: " + map_data);
            System.out.println("jsonNode: \n" + jsonNode);
//            String jsonString = objectMapper.writeValueAsString(jsonNode);
//            System.out.println("jsonString" + jsonString);

            String map = event.getOption("map")
                    .flatMap(ApplicationCommandInteractionOption::getValue)
                    .map(ApplicationCommandInteractionOptionValue::asString)
                    .get();

//            JsonNode mapNode = jsonNode.get("pl_goldrush");
//            System.out.println("map_node: " + mapNode);
            try{
                JsonNode mapNode = jsonNode.get(map);
                String response = "";
                response += "Current WR: " + mapNode.get("curr_time").asText() + "\n";
                response += "Previous WR: " + mapNode.get("prev_time").asText() + "\n";
                if (mapNode.get("image_proof2_link").asText().isEmpty()) {
                    response += "Proof picture: " + mapNode.get("image_proof2_link").asText() + "\n";
                } else {
                    response += "Proof picture 1: " + mapNode.get("image_proof1_link").asText() + "\n";
                    response += "Proof picture 2: " + mapNode.get("image_proof2_link").asText() + "\n";
                    response += "Proof picture 3: " + mapNode.get("image_proof3_link").asText() + "\n";
                }

                if (!mapNode.get("video_proof_link").asText().isEmpty()) {
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




        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }


    }
}
