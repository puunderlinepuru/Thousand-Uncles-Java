package com.thousand_uncles.discord_bot;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.thousand_uncles.discord_bot.data.models.AnyPercentMapRecord;
import com.thousand_uncles.discord_bot.data.models.MapRecord;
import com.thousand_uncles.discord_bot.data.models.SoloMapRecord;

import java.util.Objects;

public class Test {

    public static void main(String[] args) {

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            AnyPercentMapRecord anyPercentMapRecord = new AnyPercentMapRecord();
            anyPercentMapRecord.setId(1);
            anyPercentMapRecord.setMap_name("test");
            anyPercentMapRecord.setCurr_wr_seconds((short) 1);
            anyPercentMapRecord.setPrev_wr_seconds((short) 1);
            anyPercentMapRecord.setProof_img_1_link("link");

            String category = "any";
            ObjectNode objectNode = objectMapper.valueToTree(anyPercentMapRecord);
            objectNode.put("category", category);


            String jsonString = objectMapper.writeValueAsString(objectNode);

            System.out.println(" created json string: " + jsonString);
//            AnyPercentMapRecord transformedRecord = objectMapper.readValue(jsonString, AnyPercentMapRecord.class);


            ObjectNode mapNode = (ObjectNode) objectMapper.readTree(jsonString);
            String gotCategory = mapNode.get("category").asText();

            mapNode.remove("category");
            if (Objects.equals(gotCategory, "any")){
                System.out.println(" got any ");
            }
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
                case "solo":
                    SoloMapRecord soloTransformedRecord = objectMapper.treeToValue(mapNode, SoloMapRecord.class);
                    System.out.println(" extracted record: \n" +
                            "ID: " + soloTransformedRecord.getId() + "\n" +
                            "Name: " + soloTransformedRecord.getMap_name() + "\n" +
                            "Curr WR: " + soloTransformedRecord.getCurr_wr_seconds() + "\n" +
                            "Prev WR: " + soloTransformedRecord.getPrev_wr_seconds() + "\n" +
                            "Proof pic: " + soloTransformedRecord.getProof_img_1_link()
                    );
                default:
                    System.out.println("default trigger");
            }


        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

    }
}
