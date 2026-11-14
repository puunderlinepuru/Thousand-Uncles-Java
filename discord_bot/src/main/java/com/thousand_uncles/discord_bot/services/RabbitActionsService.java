package com.thousand_uncles.discord_bot.services;

import com.thousand_uncles.data.dto.RecordRequestEnvelope;
import com.thousand_uncles.data.models.common.ManualIndexedMapRecordEntry;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.AsyncRabbitTemplate;
import org.springframework.amqp.rabbit.RabbitConverterFuture;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.node.ObjectNode;

import java.math.BigDecimal;
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

    public BigDecimal askForMapTime(String mapName, String recordCategory){
        RecordRequestEnvelope responseEnvelope = makeRequest("just_time", mapName, recordCategory);
        return  (BigDecimal) responseEnvelope.getData();
    }

    public ManualIndexedMapRecordEntry askForMapRecord(String mapName, String recordCategory){
        RecordRequestEnvelope responseEnvelope = makeRequest("just_time", mapName, recordCategory);
        return  (ManualIndexedMapRecordEntry) responseEnvelope.getData();
    }

    private RecordRequestEnvelope makeRequest(String type, String mapName, String recordCategory){
        ObjectNode message = new ObjectMapper().createObjectNode();

        message.put("map_name", mapName);
        message.put("category", recordCategory);
        RecordRequestEnvelope requestEnvelope = new RecordRequestEnvelope(type, message);

        RabbitConverterFuture<RecordRequestEnvelope> response = asyncRabbitTemplate.convertSendAndReceive(
                directMapRecordsExchange.getName(),
                ROUTING_KEY,
                requestEnvelope
        );
        try{
            return response.get();
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
