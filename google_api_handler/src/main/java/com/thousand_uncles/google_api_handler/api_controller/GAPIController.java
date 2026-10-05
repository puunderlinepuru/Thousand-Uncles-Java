package com.thousand_uncles.google_api_handler.api_controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/")
@SuppressWarnings("unused")
public class GAPIController {

    @PostMapping("/test_function")
    public String publishPoll(@RequestBody String duration){
//        botActionsService.publishCavePoll(Integer.parseInt(duration));
//        TODO substitute RabbitActionsService call with tracker API call
//        rabbitActionsService.sendToCommand("server1", "PrintCenterTextAll", "test message");
        return "alright";
    }
}
