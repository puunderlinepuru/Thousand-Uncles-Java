package com.thousand_uncles.discord_bot.listeners.rabbit;

import com.thousand_uncles.discord_bot.services.BotActionsService;
import org.springframework.beans.factory.annotation.Autowired;
import tools.jackson.databind.JsonNode;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

@SuppressWarnings("unused")
@Component
public class MessagesToPostListener {

    @Autowired
    BotActionsService botActionsService;

    @Bean
    public DirectExchange discordMessageExchange() {
        return new DirectExchange("discord.messages.exchange");
    }

    @Bean
    public Queue discordMessageQueue() {
        return new Queue("discord.messages.topost");
    }

    @SuppressWarnings("unused")
    @Bean
    public Binding discordMessageBinding() {
        return BindingBuilder.bind(discordMessageQueue()).to(discordMessageExchange()).with("discord.messages.routing.key");
    }

    @RabbitListener(queues = "discord.messages.topost")
    public void receiveMessage(String message) {
        System.out.println("Received message: " + message);

        JsonNode jsonNode;

        jsonNode = processJSON(message);
        String channel = jsonNode.get("channel").asString();
        String messageContents = jsonNode.get("message").asString();

        switch (channel) {
            case "the-cave" -> botActionsService.sendIntoCave(messageContents);
            case "currently-gaming" -> botActionsService.sendIntoCurrentlyGaming(messageContents);
            default -> throw new IllegalStateException("Unexpected value: " + channel);
        }
    }

    private JsonNode processJSON(String message) {
        ObjectMapper objectMapper = new ObjectMapper();
         return objectMapper.readTree(message);
    }
}
