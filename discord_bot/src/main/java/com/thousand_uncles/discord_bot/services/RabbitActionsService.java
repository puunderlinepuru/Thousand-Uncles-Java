package com.thousand_uncles.discord_bot.services;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;

//Probably will be used but not today

//@Component
public class RabbitActionsService {

    @Autowired
    RabbitTemplate rabbitTemplate;

}
