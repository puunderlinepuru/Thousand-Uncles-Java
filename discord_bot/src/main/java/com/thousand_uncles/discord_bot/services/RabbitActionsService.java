package com.thousand_uncles.discord_bot.services;

import com.thousand_uncles.data.models.common.ManualIndexedMapRecordEntry;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.AsyncRabbitTemplate;
import org.springframework.amqp.rabbit.RabbitConverterFuture;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.node.ObjectNode;

import java.util.concurrent.ExecutionException;

//Probably will be used but not today

@Component
public class RabbitActionsService {

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Autowired
    AsyncRabbitTemplate asyncRabbitTemplate;

    private final DirectExchange directMapRecordsExchange;
    public static final String ROUTING_KEY = "map.record";

    public RabbitActionsService(DirectExchange directMapRecordsExchange) {
        this.directMapRecordsExchange = directMapRecordsExchange;
    }

    public String askForMapTime(){
        ObjectNode message = new ObjectMapper().createObjectNode();

        message.put("type", "full");

        RabbitConverterFuture<String> response = asyncRabbitTemplate.convertSendAndReceive(
                directMapRecordsExchange.getName(),
                ROUTING_KEY,
                message.toString()
        );

//        botActionsService.publishCavePoll(Integer.parseInt(duration));
//        TODO substitute RabbitActionsService call with tracker API call
//        rabbitActionsService.sendToCommand("server1", "PrintCenterTextAll", "test message");
        try{
            return response.get();
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
