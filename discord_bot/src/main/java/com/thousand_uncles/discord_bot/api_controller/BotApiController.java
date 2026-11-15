package com.thousand_uncles.discord_bot.api_controller;

import com.thousand_uncles.discord_bot.common.util.AppNotifications;
import com.thousand_uncles.discord_bot.config.GlobalConfigThings;
import com.thousand_uncles.discord_bot.services.BotActionsService;
import com.thousand_uncles.discord_bot.services.RabbitActionsService;
import discord4j.core.GatewayDiscordClient;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.*;
import tools.jackson.databind.node.ObjectNode;

@Profile("friendly")
@RestController
@RequestMapping("/api/bot")
@SuppressWarnings("unused")
public class BotApiController {

    @Autowired
    RabbitActionsService rabbitActionsService;

    private final DirectExchange directMapRecordsExchange;
    public static final String ROUTING_KEY = "map.record";

    @Autowired
    GatewayDiscordClient client;

    @Autowired
    BotActionsService botActionsService;

    @Autowired
    GlobalConfigThings globalConfigThings;

    public BotApiController(DirectExchange directMapRecordsExchange) {
        this.directMapRecordsExchange = directMapRecordsExchange;
    }

    @PostMapping("/send/{channel_name}")
    public String sendIntoChannel(@PathVariable String channel_name, @RequestBody String message){
        AppNotifications.API.API_INFO("Call to say: " + message + "\n channel: " + channel_name);
        return switch (channel_name){
            case "currently-gaming" -> {
                botActionsService.sendIntoCurrentlyGaming(message);
                yield "sent";
            }
            case "the-cave" -> {
                botActionsService.sendIntoCave(message);
                yield "sent";
            }
            default -> "couldn't send message";
        };
    }

    @PostMapping("/test_function")
    public String testFunction(@RequestBody String request){

        ObjectNode requestNode = globalConfigThings.ObjectMapper().readTree(request).asObject();

        String type = requestNode.get("type").asString();
        String mapName = requestNode.get("map_name").asString();
        String category = requestNode.get("category").asString();

        return switch (type){
            case "1" -> rabbitActionsService.askForMapTime(mapName, category).toString();
            case "2" -> rabbitActionsService.askForMapRecord(mapName, category).toString();
            default -> "wrong";
        };
    }
}
