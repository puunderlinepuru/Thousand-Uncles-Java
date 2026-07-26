package com.thousand_uncles.discord_bot;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Test {
    public static void main(String[] args) {
        System.out.println(NumberToString(BigDecimal.valueOf(1147.140014)));
    }

    private static void precision(String timeString){
        double aDouble = Double.parseDouble(timeString);
        System.out.printf("%.6f%n", aDouble);
    }

    public static String NumberToString (BigDecimal timeNumber) {
        String timeString;
        BigDecimal minutes = timeNumber.divide(BigDecimal.valueOf(60),0, RoundingMode.HALF_UP);
        BigDecimal seconds = timeNumber.remainder(BigDecimal.valueOf(60)).setScale(2, RoundingMode.HALF_UP);
        if (seconds.compareTo(BigDecimal.valueOf(10)) <0){
            timeString = minutes + ":0" + seconds;
        } else {
            timeString = minutes + ":" + seconds;
        }
        return timeString;
    }

    private static void encoding(){
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode objectNode = objectMapper.createObjectNode();

        objectNode.put("test", "thing");

        System.out.println("as text:" + objectNode);

        byte[] utf8Bytes = objectNode.toString().getBytes();
        StringBuilder hexBuilder = new StringBuilder();
        for (byte b : utf8Bytes) {
            hexBuilder.append(String.format("%02x", b).toUpperCase());
        }
        System.out.println("hex: " + hexBuilder);
    }
}