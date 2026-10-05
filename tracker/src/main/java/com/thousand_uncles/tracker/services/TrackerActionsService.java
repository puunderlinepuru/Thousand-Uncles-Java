package com.thousand_uncles.tracker.services;

import com.thousand_uncles.tracker.config.TrackerConfig;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class TrackerActionsService {
    TrackerConfig trackerConfig;

    private final TrackerConfig.Api_Addresses configApiAddresses;

    RestTemplate restTemplate = new RestTemplate();

    TrackerActionsService(TrackerConfig trackerConfig){
        this.trackerConfig = trackerConfig;
        configApiAddresses = trackerConfig.getApi_addresses();
    }

    public String postMessageToDiscordAPI(String channel, String message){
        System.out.println("sending [" + message + "] to [" + configApiAddresses.getDiscord_bot()+channel+"]");
        return restTemplate.postForObject(configApiAddresses.getDiscord_bot()+channel, message, String.class);
    }
}
