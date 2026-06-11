package com.thousand_uncles.google_api_handler.app.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.thousand_uncles.google_api_handler.data.models.AnyPercentMapRecord;
import com.thousand_uncles.google_api_handler.data.models.MapRecord;
import com.thousand_uncles.google_api_handler.data.models.SoloMapRecord;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Objects;

@Component
public class MessageReceiver {

    @RabbitListener(queues = "test.queue")
    public void receiveMessage(String message) {
        System.out.println("Received message: " + message);
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                ObjectNode mapNode = (ObjectNode) objectMapper.readTree(message);
                String gotCategory = mapNode.get("category").asText();

                mapNode.remove("category");
                switch(gotCategory){
                    case "any":
                        System.out.println("any case");
                        AnyPercentMapRecord transformedRecord = objectMapper.treeToValue(mapNode, AnyPercentMapRecord.class);
                        System.out.println(" extracted record: \n" +
                                "ID: " + transformedRecord.getId() + "\n" +
                                "Name: " + transformedRecord.getMap_name() + "\n" +
                                "Curr WR: " + transformedRecord.getCurr_wr_seconds() + "\n" +
                                "Prev WR: " + transformedRecord.getPrev_wr_seconds() + "\n" +
                                "Proof pic: " + transformedRecord.getProof_img_1_link()
                        );
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
                        break;
                }


//                UpdateValues.updateSheets("1YtpbwvqTOiBRN4Sm9SXlNJCq6qPb2dvW3SBmwu5poNs", List );
            } catch (JsonMappingException e) {
                throw new RuntimeException(e);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
    }
}
