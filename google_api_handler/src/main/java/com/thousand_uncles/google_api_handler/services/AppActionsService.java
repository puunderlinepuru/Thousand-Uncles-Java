package com.thousand_uncles.google_api_handler.services;

import com.thousand_uncles.google_api_handler.config.GAPIConfig;
import org.springframework.web.client.RestTemplate;
import com.thousand_uncles.google_api_handler.util.GlobalThings.GameServerCommand;

public class AppActionsService {
    GAPIConfig appConfig;

    private final GAPIConfig.Api_Addresses configApiAddresses;

    RestTemplate restTemplate = new RestTemplate();

    AppActionsService(GAPIConfig appConfig){
        this.appConfig = appConfig;
        configApiAddresses = appConfig.getApi_addresses();
    }

    public String postMessageToDiscordAPI(String message){
        return restTemplate.postForObject(configApiAddresses.getDiscord_bot(), message, String.class);
    }

    public String postCommandToGameServer(int serverID, GameServerCommand command, String message){

        return switch (command){
            case PrintToChatAll -> {
                yield "";
            }
            case PrintCenterTextAll -> {
                yield null;
            }
            case null, default -> {
                yield null;
            }
        };
//        TODO make DTO and make request with JSON
        //        rabbitActionsService.sendToCommand("server1", "PrintCenterTextAll", "test message");
    }
}
