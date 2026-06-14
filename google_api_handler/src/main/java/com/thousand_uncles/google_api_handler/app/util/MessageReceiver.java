package com.thousand_uncles.google_api_handler.app.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.thousand_uncles.google_api_handler.data.models.AnyPercentMapRecord;
import com.thousand_uncles.google_api_handler.data.models.MapRecord;
import com.thousand_uncles.google_api_handler.data.models.SoloMapRecord;
import com.thousand_uncles.google_api_handler.data.service.MapRecordServiceProd;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

@Component
public class MessageReceiver {
    @Autowired
    MapRecordServiceProd mapRecordServiceProd;


    @RabbitListener(queues = "test.queue")
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

                        int mapID = MapOrderHandler.getMapOrderList().indexOf(anyPercentTransformedRecord.getMap_name())+3;

                        System.out.println("cell value:" + mapID);

                        UpdateValues.updateSheets(List.of(data), "Any%", "A" + mapID);
                        try{
                            mapRecordServiceProd.updateAny(
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
                        System.out.println(" extracted record: \n" +
                                "ID: " + soloTransformedRecord.getId() + "\n" +
                                "Name: " + soloTransformedRecord.getMap_name() + "\n" +
                                "Curr WR: " + soloTransformedRecord.getCurr_wr_seconds() + "\n" +
                                "Prev WR: " + soloTransformedRecord.getPrev_wr_seconds() + "\n" +
                                "Proof pic: " + soloTransformedRecord.getProof_img_1_link()
                        );
                        UpdateValues.updateSheets(List.of(data), "Solo%", "A" + MapOrderHandler.getMapOrderList().indexOf(soloTransformedRecord.getMap_name())+3);
                        break;
                }
            } catch (JsonMappingException e) {
                throw new RuntimeException(e);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
    }
}
