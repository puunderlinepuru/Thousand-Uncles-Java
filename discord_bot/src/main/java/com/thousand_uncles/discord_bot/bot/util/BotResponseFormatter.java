package com.thousand_uncles.discord_bot.bot.util;

import com.thousand_uncles.discord_bot.data.models.MapRecord;
import com.thousand_uncles.discord_bot.data.util.RecordFormatter;

public class BotResponseFormatter {

    public static String mapRecordToMessageContent(MapRecord requestedMap) {
        String response = "";
        response += "Current WR: " + RecordFormatter.NumberToString(requestedMap.getCurr_wr_seconds()) + "\n";
        response += "Previous WR: " + RecordFormatter.NumberToString(requestedMap.getPrev_wr_seconds()) + "\n";
        if (requestedMap.getProof_img_2_link() == null) {
            response += "Proof picture: " + requestedMap.getProof_img_1_link() + "\n";
        } else {
            response += "Proof picture 1: " + requestedMap.getProof_img_1_link() + "\n";
            response += "Proof picture 2: " + requestedMap.getProof_img_2_link() + "\n";
            response += "Proof picture 3: " + requestedMap.getProof_img_3_link() + "\n";

            try {
                response += "Stage 1 time: " + RecordFormatter.NumberToString(requestedMap.getStage_1_time_seconds()) + "\n";
                response += "Stage 2 time: " + RecordFormatter.NumberToString(requestedMap.getStage_2_time_seconds()) + "\n";
                response += "Stage 3 time: " + RecordFormatter.NumberToString(requestedMap.getStage_3_time_seconds()) + "\n";
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        if (requestedMap.getProof_vid_link() != null) {
            response+= "Video: " + requestedMap.getProof_vid_link();
        }

        return response;
    }
}
