package com.thousand_uncles.google_api_handler.app.listeners;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.thousand_uncles.google_api_handler.app.util.MapOrderHandler;
import com.thousand_uncles.google_api_handler.app.spreadsheet.UpdateValues;
import com.thousand_uncles.google_api_handler.data.models.AnyPercentMapRecord;
import com.thousand_uncles.google_api_handler.data.models.SoloMapRecord;
import com.thousand_uncles.google_api_handler.data.service.MapRecordServiceProd;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

import java.util.*;

@SuppressWarnings("unused")
//@Component
public class ValidateListener {
    @Autowired
    MapRecordServiceProd mapRecordServiceProd;

    @Bean
    public DirectExchange validateExchange() {
        return new DirectExchange("validate.exchange");
    }

    @Bean
    public org.springframework.amqp.core.Queue validateQueue() {
        return new Queue("validate.queue");
    }

    @SuppressWarnings("unused")
    @Bean
    public Binding validateBinding() {
        return BindingBuilder.bind(validateQueue()).to(validateExchange()).with("validate.routing.key");
    }

    @RabbitListener(queues = "validate.queue")
    public void receiveMessage(String message) {
        System.out.println("Received message: " + message);
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                ObjectNode mapNode = (ObjectNode) objectMapper.readTree(message);
                String gotCategory = mapNode.get("category").asText();
                mapNode.remove("category");
                List<Object> data = new ArrayList<>();
                for(JsonNode element : mapNode){
                    data.add(element.asText());
                }

                int mapID;

                switch(gotCategory){
                    case "any":
                        AnyPercentMapRecord anyPercentTransformedRecord = objectMapper.treeToValue(mapNode, AnyPercentMapRecord.class);
                        System.out.println(" extracted record: \n" +
                                "ID: " + anyPercentTransformedRecord.getId() + "\n" +
                                "Name: " + anyPercentTransformedRecord.getMap_name() + "\n" +
                                "Curr WR: " + anyPercentTransformedRecord.getCurr_wr_seconds() + "\n" +
                                "Prev WR: " + anyPercentTransformedRecord.getPrev_wr_seconds() + "\n" +
                                "Proof pic: " + anyPercentTransformedRecord.getProof_img_1_link()
                        );

                        mapID = MapOrderHandler.getMapOrderList().indexOf(anyPercentTransformedRecord.getMap_name())+3;

                        System.out.println("cell value:" + mapID);

                        UpdateValues.updateSheets(List.of(data), "Any%", "A" + mapID);
                        try{
                            mapRecordServiceProd.updateAny(
                                    anyPercentTransformedRecord.getId(),
                                    anyPercentTransformedRecord.getMap_name(),
                                    anyPercentTransformedRecord.getCurr_wr_seconds(),
                                    anyPercentTransformedRecord.getProof_img_1_link(),
                                    anyPercentTransformedRecord.getProof_img_2_link(),
                                    anyPercentTransformedRecord.getProof_img_3_link(),
                                    anyPercentTransformedRecord.getProof_vid_link(),
                                    anyPercentTransformedRecord.getStage_1_time_seconds(),
                                    anyPercentTransformedRecord.getStage_2_time_seconds(),
                                    anyPercentTransformedRecord.getStage_3_time_seconds()
                            );
                        } catch (NoSuchElementException e){
                            mapRecordServiceProd.saveAny(
                                    anyPercentTransformedRecord.getId(),
                                    anyPercentTransformedRecord.getMap_name(),
                                    anyPercentTransformedRecord.getCurr_wr_seconds(),
                                    anyPercentTransformedRecord.getPrev_wr_seconds(),
                                    anyPercentTransformedRecord.getProof_img_1_link(),
                                    anyPercentTransformedRecord.getProof_img_2_link(),
                                    anyPercentTransformedRecord.getProof_img_3_link(),
                                    anyPercentTransformedRecord.getProof_vid_link(),
                                    anyPercentTransformedRecord.getStage_1_time_seconds(),
                                    anyPercentTransformedRecord.getStage_2_time_seconds(),
                                    anyPercentTransformedRecord.getStage_3_time_seconds()
                            );
                        }
                        break;
                    case "solo":
                        SoloMapRecord soloTransformedRecord = objectMapper.treeToValue(mapNode, SoloMapRecord.class);

                        mapID = MapOrderHandler.getMapOrderList().indexOf(soloTransformedRecord.getMap_name())+3;

                        System.out.println("cell value:" + mapID);

                        System.out.println(" extracted record: \n" +
                                "ID: " +        soloTransformedRecord.getId() + "\n" +
                                "Name: " +      soloTransformedRecord.getMap_name() + "\n" +
                                "The Hero: " +  soloTransformedRecord.getThe_hero() + "\n" +
                                "Curr WR: " +   soloTransformedRecord.getCurr_wr_seconds() + "\n" +
                                "Prev WR: " +   soloTransformedRecord.getPrev_wr_seconds() + "\n" +
                                "Proof pic: " + soloTransformedRecord.getProof_img_1_link()
                        );
                        UpdateValues.updateSheets(List.of(data), "Solo%", "A" + MapOrderHandler.getMapOrderList().indexOf(soloTransformedRecord.getMap_name())+3);
                        try{
                            mapRecordServiceProd.updateSolo(
                                    soloTransformedRecord.getId(),
                                    soloTransformedRecord.getMap_name(),
                                    soloTransformedRecord.getCurr_wr_seconds(),
                                    soloTransformedRecord.getThe_hero(),
                                    soloTransformedRecord.getProof_img_1_link(),
                                    soloTransformedRecord.getProof_img_2_link(),
                                    soloTransformedRecord.getProof_img_3_link(),
                                    soloTransformedRecord.getProof_vid_link(),
                                    soloTransformedRecord.getStage_1_time_seconds(),
                                    soloTransformedRecord.getStage_2_time_seconds(),
                                    soloTransformedRecord.getStage_3_time_seconds()
                            );
                        } catch (NoSuchElementException e){
                            mapRecordServiceProd.saveSolo(
                                    soloTransformedRecord.getId(),
                                    soloTransformedRecord.getMap_name(),
                                    soloTransformedRecord.getCurr_wr_seconds(),
                                    soloTransformedRecord.getPrev_wr_seconds(),
                                    soloTransformedRecord.getThe_hero(),
                                    soloTransformedRecord.getProof_img_1_link(),
                                    soloTransformedRecord.getProof_img_2_link(),
                                    soloTransformedRecord.getProof_img_3_link(),
                                    soloTransformedRecord.getProof_vid_link(),
                                    soloTransformedRecord.getStage_1_time_seconds(),
                                    soloTransformedRecord.getStage_2_time_seconds(),
                                    soloTransformedRecord.getStage_3_time_seconds()
                            );
                        }
                        break;
                }
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
    }
}
