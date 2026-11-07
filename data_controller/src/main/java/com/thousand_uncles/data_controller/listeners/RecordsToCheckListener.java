package com.thousand_uncles.data_controller.listeners;

import com.thousand_uncles.data.service.MapRecordServiceOnline;
import com.thousand_uncles.data_controller.services.DataControllerActionsService;
import com.thousand_uncles.data_controller.services.RabbitQueuePurgeService;
import com.thousand_uncles.data_controller.util.GlobalThings;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import tools.jackson.databind.JsonNode;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

@SuppressWarnings("unused")
@Component
public class RecordsToCheckListener {

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Autowired
    MapRecordServiceOnline mapRecordServiceOnline;

    RecordsToCheckListener(RabbitQueuePurgeService rabbitQueuePurgeService){
        rabbitQueuePurgeService.purgeQueue("data.controller.requests");
    }

    @Bean
    public DirectExchange discordMessageExchange() {
        return new DirectExchange("map.records.checking");
    }

    @Bean
    public Queue discordMessageQueue() {
        return new Queue("data.controller.requests");
    }

    @SuppressWarnings("unused")
    @Bean
    public Binding discordMessageBinding() {
        return BindingBuilder.bind(discordMessageQueue()).to(discordMessageExchange()).with("map.record");
    }

    @RabbitListener(queues = "data.controller.requests")
    public String receiveAndRespond(String messageString) {
        System.out.println("Received message: " + messageString);

        JsonNode messageNode = GlobalThings.getObjectMapper().readTree(messageString);

        String type = messageNode.get("type").asString();
        System.out.println(type);

        return "hi";

    }

    private JsonNode processJSON(String message) {
        ObjectMapper objectMapper = new ObjectMapper();
         return objectMapper.readTree(message);
    }
}
