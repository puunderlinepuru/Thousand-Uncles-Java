package com.thousand_uncles.data_controller.listeners;

import com.thousand_uncles.data.config.GlobalConfigThings;
import com.thousand_uncles.data.service.MapRecordServiceOnline;
import com.thousand_uncles.data_controller.services.RabbitQueuePurgeService;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import tools.jackson.databind.JsonNode;

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

    @RabbitListener(queues = "data.controller.requests")
    public String receiveAndRespond(String request) {
        JsonNode requestNode = globalConfigThings.ObjectMapper().readTree(request);

        String type = requestNode.get("type").asString();
        String mapName = requestNode.get("map_name").asString();
        String recordCategory = requestNode.get("category").asString();

        System.out.println(type);

        return switch (type){
            case "full_record" -> {
                yield getFullNode(mapName, recordCategory);
            }
            case "just_time" -> {
                yield getWRtime(mapName, recordCategory);
            }
            default -> returnError();
        };
    }

    private String getWRtime(String mapName, String recordCategory){
        String unsuffixedMapName = globalConfigThings.mapSuffixes().get(mapName);
        int mapID;
        if (unsuffixedMapName == null){
            mapID = globalConfigThings.mapIDS().indexOf(mapName);
        } else {
            mapID = globalConfigThings.mapIDS().indexOf(unsuffixedMapName);
        }

        BigDecimal WRtime = mapRecordServiceOnline.getCurrentWR(mapID, recordCategory);

        return "{" +
                "\"time\": " + WRtime.toString() +
                "}";
    }

    private String getFullNode(String mapName, String recordCategory){
        String unsuffixedMapName = globalConfigThings.mapSuffixes().get(mapName);
        int mapID;
        if (unsuffixedMapName == null){
            mapID = globalConfigThings.mapIDS().indexOf(mapName);
        } else {
            mapID = globalConfigThings.mapIDS().indexOf(unsuffixedMapName);
        }
        return mapRecordServiceOnline.getRecord(mapID, recordCategory).toString();
    }

    private String returnError(){
        return
            "{" +
                "\"error\": \"Error determining request type. Is \"type\" field present?\"" +
            "}";

    }
}
