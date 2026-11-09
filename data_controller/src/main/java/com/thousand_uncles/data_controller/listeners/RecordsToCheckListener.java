package com.thousand_uncles.data_controller.listeners;

import com.thousand_uncles.data.service.MapRecordServiceOnline;
import com.thousand_uncles.data_controller.services.RabbitQueuePurgeService;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.node.ObjectNode;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unused")
@Component
public class RecordsToCheckListener {

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Autowired
    MapRecordServiceOnline mapRecordServiceOnline;

    private final ObjectMapper objectMapper;
    public List<String> mapIDS;
    public Map<String, String> mapSuffixes;

    RecordsToCheckListener(
            RabbitQueuePurgeService rabbitQueuePurgeService,
            ObjectMapper objectMapper,
            List<String> mapIDS,
            Map<String, String> mapSuffixes
    ){
        rabbitQueuePurgeService.purgeQueue("data.controller.requests");
        this.objectMapper = objectMapper;
        this.mapIDS = mapIDS;
        this.mapSuffixes = mapSuffixes;
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

        ObjectNode requestNode = objectMapper.readTree(messageString).asObject();
        String type = requestNode.get("type").asString();
        requestNode.remove("type");
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

    private String getWRtime(String mapName, String recordCategory){
        String unsuffixedMapName = mapSuffixes.get(mapName);
        int mapID;
        if (unsuffixedMapName == null){
            mapID = mapIDS.indexOf(mapName);
        } else {
            mapID = mapIDS.indexOf(unsuffixedMapName);
        }

        BigDecimal WRtime = mapRecordServiceOnline.getCurrentWR(mapID, recordCategory);

        return
                "{" +
                "\"time\": " + WRtime +
                "}";
    }

    private String getFullNode(String mapName, String recordCategory){
        String unsuffixedMapName = mapSuffixes.get(mapName);
        int mapID;
        if (unsuffixedMapName == null){
            mapID = mapIDS.indexOf(mapName);
        } else {
            mapID = mapIDS.indexOf(unsuffixedMapName);
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
