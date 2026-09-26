package com.thousand_uncles.discord_bot.api_controller;

import com.thousand_uncles.discord_bot.services.BotActionsService;
import discord4j.core.GatewayDiscordClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/bot")
@SuppressWarnings("unused")
public class BotApiController {

    @Autowired
    GatewayDiscordClient client;

    @Autowired
    BotActionsService botActionsService;

    @PostMapping("/send/currently-gaming")
    public String sendIntoCurrentlyGaming(@RequestBody String message){
//        botActionsService.sendIntoCave(message);
        botActionsService.sendIntoCurrentlyGaming(message);
        return "hi";
    }

    @PostMapping("/send/cave")
    public String sendIntoCave(@RequestBody String message){
        botActionsService.sendIntoCave(message);
        return "hi";
    }

    @PostMapping("/test_function")
    public String publishPoll(@RequestBody String duration){
//        botActionsService.publishCavePoll(Integer.parseInt(duration));
//        TODO substitute RabbitActionsService call with tracker API call
//        rabbitActionsService.sendToCommand("server1", "PrintCenterTextAll", "test message");
        return "alright";
    }
}
