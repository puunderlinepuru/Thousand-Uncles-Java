package com.thousand_uncles.discord_bot.services;

import com.thousand_uncles.discord_bot.config.GlobalConfigThings;
import com.thousand_uncles.discord_bot.dto.ManualIndexedMapRecordEntry;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tools.jackson.databind.node.ObjectNode;

import java.math.BigDecimal;

@Component
public class RabbitActionsService {

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Autowired
    GlobalConfigThings globalConfigThings;

    private final DirectExchange directMapRecordsExchange;
    public static final String ROUTING_KEY = "map.record";

    public RabbitActionsService(DirectExchange directMapRecordsExchange) {
        this.directMapRecordsExchange = directMapRecordsExchange;
    }

    public BigDecimal askForMapTime(String mapName, String recordCategory){
        String responseString = makeRequest("just_time", mapName, recordCategory);
        ObjectNode responseNode = globalConfigThings.ObjectMapper().readTree(responseString).asObject();
        return responseNode.get("time").asDecimal();
    }

    public ManualIndexedMapRecordEntry askForMapRecord(String partialMapName, String recordCategory){
        String responseString = makeRequest("full_record", partialMapName, recordCategory);
        return globalConfigThings.ObjectMapper().readValue(responseString, ManualIndexedMapRecordEntry.class);
    }

    private String makeRequest(String type, String mapName, String recordCategory){
        ObjectNode message = globalConfigThings.ObjectMapper().createObjectNode();

        message.put("type", type);
        message.put("map_name", mapName);
        message.put("category", recordCategory);

        return String.valueOf(rabbitTemplate.convertSendAndReceive(
                directMapRecordsExchange.getName(),
                ROUTING_KEY,
                globalConfigThings.ObjectMapper().writeValueAsString(message)
        ));
    }
}
