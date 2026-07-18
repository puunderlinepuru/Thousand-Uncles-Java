package com.thousand_uncles.discord_bot.bot.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RabbitActionsService {

    @Autowired
    public DirectExchange eventExchange;

    @Autowired
    public org.springframework.amqp.core.Queue eventQueue;

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Autowired
    public Binding eventBinding;

    public void sendToEvent(JsonNode jsonNode){
        try {
            String jsonString = convertJSONtoString(jsonNode);
            assert jsonString != null;
            rabbitTemplate.convertAndSend("test.exchange", "test.routing.key", jsonString);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendToValidate(JsonNode jsonNode){
        try {
            String jsonString = convertJSONtoString(jsonNode);
            assert jsonString != null;
            rabbitTemplate.convertAndSend("validate.exchange", "validate.routing.key", jsonString);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private String convertJSONtoString(JsonNode jsonNode) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(jsonNode);
        return null;
    }
}
