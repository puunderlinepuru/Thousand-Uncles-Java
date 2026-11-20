package com.thousand_uncles.discord_bot.util;

import com.thousand_uncles.discord_bot.dto.ManualIndexedMapRecordEntry;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class DiscordBotResponseFormatter {

    public static String mapRecordToMessageContent(ManualIndexedMapRecordEntry requestedMap) {
        String response = "";
        response += "Current WR: " + BigDecimalToString(requestedMap.getCurr_wr_seconds()) + "\n";
        response += "Previous WR: " + BigDecimalToString(requestedMap.getPrev_wr_seconds()) + "\n";
        if (requestedMap.getProof_img_2_link() == null) {
            response += "Proof picture: " + requestedMap.getProof_img_1_link() + "\n";
        } else {
            response += "Proof picture 1: " + requestedMap.getProof_img_1_link() + "\n";
            response += "Proof picture 2: " + requestedMap.getProof_img_2_link() + "\n";
            response += "Proof picture 3: " + requestedMap.getProof_img_3_link() + "\n";

            try {
                response += "Stage 1 time: " + BigDecimalToString(requestedMap.getStage_1_time_seconds()) + "\n";
                response += "Stage 2 time: " + BigDecimalToString(requestedMap.getStage_2_time_seconds()) + "\n";
                response += "Stage 3 time: " + BigDecimalToString(requestedMap.getStage_3_time_seconds()) + "\n";
            } catch (Exception e) {
                System.out.println("Error converting stage times, everything is fine.");
            }
        }

        if (requestedMap.getProof_vid_link() != null) {
            response+= "Video: " + requestedMap.getProof_vid_link();
        }

        return response;
    }

    @SuppressWarnings("unused")
    public static String BigDecimalToString(BigDecimal timeNumber) {
        String timeString;
        BigDecimal result[] = timeNumber.divideAndRemainder(BigDecimal.valueOf(60));
        int minutes = result[0].intValue();
        int seconds = result[1].intValue();
        BigDecimal milliseconds = timeNumber.remainder(BigDecimal.ONE);
        if (seconds < 10){
            timeString = minutes + ":0" + seconds;
        } else {
            timeString = minutes + ":" + seconds;
        }

        if (milliseconds.equals(BigDecimal.ZERO)){
            timeString += ".99";
        } else {
            timeString += milliseconds.toPlainString().substring(1);
        }
        return timeString;
    }
}
