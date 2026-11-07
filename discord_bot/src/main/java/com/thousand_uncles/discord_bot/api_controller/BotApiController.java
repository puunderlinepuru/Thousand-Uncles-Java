package com.thousand_uncles.discord_bot.api_controller;

import com.thousand_uncles.discord_bot.common.util.AppNotifications;
import com.thousand_uncles.discord_bot.services.BotActionsService;
import com.thousand_uncles.discord_bot.services.RabbitActionsService;
import discord4j.core.GatewayDiscordClient;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.AsyncRabbitTemplate;
import org.springframework.amqp.rabbit.RabbitConverterFuture;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.bind.annotation.*;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.node.ObjectNode;

import java.util.concurrent.ExecutionException;

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
    public String publishPoll(){
        return rabbitActionsService.askForMapTime();
    }
}
