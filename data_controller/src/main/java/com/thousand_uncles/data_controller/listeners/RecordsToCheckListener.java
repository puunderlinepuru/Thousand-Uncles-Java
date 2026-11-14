package com.thousand_uncles.data_controller.listeners;

import com.thousand_uncles.data.config.GlobalConfigThings;
import com.thousand_uncles.data.dto.RecordRequestEnvelope;
import com.thousand_uncles.data.service.MapRecordServiceOnline;
import com.thousand_uncles.data_controller.services.RabbitQueuePurgeService;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import tools.jackson.databind.node.ObjectNode;

import java.math.BigDecimal;

@SuppressWarnings("unused")
@Component
public class RecordsToCheckListener {

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Autowired
    MapRecordServiceOnline mapRecordServiceOnline;

    @Autowired
    GlobalConfigThings globalConfigThings;

    RecordsToCheckListener(
            RabbitQueuePurgeService rabbitQueuePurgeService
    ){
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
    public RecordRequestEnvelope receiveAndRespond(RecordRequestEnvelope requestEnvelope) {
        System.out.println("Received message: " + requestEnvelope);

        ObjectNode requestNode = globalConfigThings.ObjectMapper().readTree(requestEnvelope.getData().toString()).asObject();
        String type = requestEnvelope.getType();
        System.out.println(type);

        String mapName = requestNode.get("map_name").asString();
        String recordCategory = requestNode.get("category").asString();

        return switch (type){
            case "full" -> {
                yield getFullNode(mapName, recordCategory);
            }
            case "just_time" -> {
                yield getWRtime(mapName, recordCategory);
            }
            default -> returnError();
        };

    }

    private RecordRequestEnvelope getWRtime(String mapName, String recordCategory){
        String unsuffixedMapName = globalConfigThings.mapSuffixes().get(mapName);
        int mapID;
        if (unsuffixedMapName == null){
            mapID = globalConfigThings.mapIDS().indexOf(mapName);
        } else {
            mapID = globalConfigThings.mapIDS().indexOf(unsuffixedMapName);
        }

        BigDecimal WRtime = mapRecordServiceOnline.getCurrentWR(mapID, recordCategory);

        return new RecordRequestEnvelope("just_time", WRtime);
    }

    private RecordRequestEnvelope getFullNode(String mapName, String recordCategory){
        String unsuffixedMapName = globalConfigThings.mapSuffixes().get(mapName);
        int mapID;
        if (unsuffixedMapName == null){
            mapID = globalConfigThings.mapIDS().indexOf(mapName);
        } else {
            mapID = globalConfigThings.mapIDS().indexOf(unsuffixedMapName);
        }
        return new RecordRequestEnvelope("full", mapRecordServiceOnline.getRecord(mapID, recordCategory));
    }

    private RecordRequestEnvelope returnError(){
        return new RecordRequestEnvelope("error",
                "{" +
                "\"error\": \"Error determining request type. Is \"type\" field present?\"" +
                "}");

    }
}
