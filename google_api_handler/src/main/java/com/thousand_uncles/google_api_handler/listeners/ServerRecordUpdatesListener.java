package com.thousand_uncles.google_api_handler.listeners;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.exc.JsonNodeException;

@SuppressWarnings("unused")
@Component
public class ServerRecordUpdatesListener {

    @Bean
    public DirectExchange updateExchange() {
        return new DirectExchange("record.update.exchange");
    }

    @Bean
    public org.springframework.amqp.core.Queue updateQueue() {
        return new Queue("server.record.updates");
    }

    @SuppressWarnings("unused")
    @Bean
    public Binding updateBinding() {
        return BindingBuilder.bind(updateQueue()).to(updateExchange()).with("server.record.updates");
    }

    @RabbitListener(queues = "server.record.updates")
    public void receiveMessage(String message) {
        System.out.println("Received message: " + message);

        JsonNode jsonNode;

        try{
            jsonNode = processJSON(message);
        } catch (JsonNodeException e) {
            throw new RuntimeException(e);
        }

        System.out.println("JSON node: " + jsonNode.toPrettyString());

    }

    private JsonNode processJSON(String message) throws JsonNodeException {
        ObjectMapper objectMapper = new ObjectMapper();
         return objectMapper.readTree(message);
    }
}
