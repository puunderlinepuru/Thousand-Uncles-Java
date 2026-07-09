package com.thousand_uncles.google_api_handler.app.listeners;

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
public class EventListener {

    @Bean
    public DirectExchange eventExchange() {
        return new DirectExchange("event.exchange");
    }

    @Bean
    public org.springframework.amqp.core.Queue eventQueue() {
        return new Queue("event.queue");
    }

    @SuppressWarnings("unused")
    @Bean
    public Binding eventBinding() {
        return BindingBuilder.bind(eventQueue()).to(eventExchange()).with("event.routing.key");
    }

    @RabbitListener(queues = "event.queue")
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
