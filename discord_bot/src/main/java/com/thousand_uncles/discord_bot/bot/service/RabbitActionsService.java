package com.thousand_uncles.discord_bot.bot.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.thousand_uncles.discord_bot.bot.util.AppNotifications;
import com.thousand_uncles.discord_bot.bot.util.GlobalThings;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;

import static com.thousand_uncles.discord_bot.bot.util.JSONHandler.convertJSONtoString;

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

    public void sendToCommand(String serverID, String command, String payload){
        if (payload == null){
            return;
        }
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        long expiration = timestamp.getTime() / 1000 + 300;

        System.out.println("expiration: " + expiration);

        ObjectNode commandNode = GlobalThings.getObjectMapper().createObjectNode();
        commandNode.put("type", "tf2_server_command");
        commandNode.put("version", 1);
        commandNode.put("id", "1");
        commandNode.put("recipient", serverID);
        commandNode.put("expires_at", expiration);
        commandNode.put("command",command);
        commandNode.put("payload", payload);

        rabbitTemplate.convertAndSend("commands.exchange", "commands.routing.key", commandNode.toString());


        AppNotifications.RabbitMQ.RABBITMQ_PUBLISH_INFO("Command sent");
//        PrintToChatAll
//        PrintCenterTextAll
    }
}
