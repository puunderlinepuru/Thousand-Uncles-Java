package com.thousand_uncles.google_api_handler.api_controller;

import com.thousand_uncles.google_api_handler.config.AppConfig;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api/")
@SuppressWarnings("unused")
public class GAPIController {

    AppConfig appConfig;

    private final AppConfig.Api_Addresses configApiAddresses;

    GAPIController(AppConfig appConfig){
        this.appConfig = appConfig;
        configApiAddresses = appConfig.getApi_addresses();
        System.out.println("api_addresses: " + configApiAddresses);
        System.out.println("URI: " + configApiAddresses.getDiscord_bot());
        System.out.println("Test: " + configApiAddresses.getTest());
    }

    RestTemplate restTemplate = new RestTemplate();

    @PostMapping("/test_function")
    public String publishPoll(@RequestBody String duration){
//        botActionsService.publishCavePoll(Integer.parseInt(duration));
//        TODO substitute RabbitActionsService call with tracker API call
//        rabbitActionsService.sendToCommand("server1", "PrintCenterTextAll", "test message");
        return "alright";
    }

    @GetMapping("/test")
    public String getTest(){
        return restTemplate.postForObject(configApiAddresses.getDiscord_bot(), "thing", String.class);
    }
}
