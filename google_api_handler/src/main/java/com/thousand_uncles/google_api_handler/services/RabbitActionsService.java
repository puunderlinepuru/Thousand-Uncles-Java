package com.thousand_uncles.google_api_handler.services;


import com.thousand_uncles.google_api_handler.util.AppNotifications;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@SuppressWarnings("unused")
@Component
public class RabbitActionsService {
    @Autowired
    RabbitTemplate rabbitTemplate;

    public void sendToDiscordMessages(String channel, String message){
        rabbitTemplate.convertAndSend("discord.messages.exchange", "discord.messages.routing.key", message);
        AppNotifications.RabbitMQ.RABBITMQ_PUBLISH_INFO("Message sent to discord");
    }
}
