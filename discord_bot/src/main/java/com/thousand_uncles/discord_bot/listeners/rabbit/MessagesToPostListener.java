package com.thousand_uncles.discord_bot.listeners.rabbit;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@SuppressWarnings("unused")
@Component
public class MessagesToPostListener {

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

        try{
            jsonNode = processJSON(message);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        System.out.println("JSON node: " + jsonNode.toPrettyString());

    }

    private JsonNode processJSON(String message) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
         return objectMapper.readTree(message);
    }
}
