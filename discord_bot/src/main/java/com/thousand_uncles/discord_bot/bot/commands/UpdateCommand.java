package com.thousand_uncles.discord_bot.bot.commands;

import com.thousand_uncles.discord_bot.bot.util.Config;
import com.thousand_uncles.discord_bot.bot.util.GlobalThings;
import com.thousand_uncles.discord_bot.data.models.MapRecord;
import com.thousand_uncles.discord_bot.data.models.SoloMapRecord;
import com.thousand_uncles.discord_bot.data.service.MapRecordService;
import com.thousand_uncles.discord_bot.data.util.RecordFormatter;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.object.command.ApplicationCommandInteractionOption;
import discord4j.core.object.command.ApplicationCommandInteractionOptionValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@SuppressWarnings("unused")
@Component
public class UpdateCommand implements SlashCommand{

    @Autowired
    Config config;

    @Autowired
    MapRecordService mapRecordService;

    @Override
    public String getName() {
        return "update";
    }

    @Override
    public Mono<Void> handle(ChatInputInteractionEvent event){
        String category = event.getOption("category")
                .flatMap(ApplicationCommandInteractionOption::getValue)
                .map(ApplicationCommandInteractionOptionValue::asString)
                .orElse(null);
        String mapName = event.getOption("map")
                .flatMap(ApplicationCommandInteractionOption::getValue)
                .map(ApplicationCommandInteractionOptionValue::asString)
                .orElse(null);
        String timeOption = event.getOption("time")
                .flatMap(ApplicationCommandInteractionOption::getValue)
                .map(ApplicationCommandInteractionOptionValue::asString)
                .orElse(null);
        String img1_link = event.getOption("proof_img_link")
                .flatMap(ApplicationCommandInteractionOption::getValue)
                .map(ApplicationCommandInteractionOptionValue::asString)
                .orElse(null);
        String img2_link = event.getOption("proof_img_link_2")
                .flatMap(ApplicationCommandInteractionOption::getValue)
                .map(ApplicationCommandInteractionOptionValue::asString)
                .orElse(null);
        String img3_link = event.getOption("proof_img_link_3")
                .flatMap(ApplicationCommandInteractionOption::getValue)
                .map(ApplicationCommandInteractionOptionValue::asString)
                .orElse(null);
        String vid_link = event.getOption("proof_vid_link")
                .flatMap(ApplicationCommandInteractionOption::getValue)
                .map(ApplicationCommandInteractionOptionValue::asString)
                .orElse(null);
        String stageTimesOption = event.getOption("stage_times")
                .flatMap(ApplicationCommandInteractionOption::getValue)
                .map(ApplicationCommandInteractionOptionValue::asString)
                .orElse(null);

        System.out.println("hello");

        int mapTime = 0, stage_1_time = 0, stage_2_time = 0, stage_3_time = 0;

//        check category for valid
        if (!config.getAvailable_categories().getUpdate().contains(category)){
            return event.reply()
                    .withEphemeral(true)
                    .withContent("Beep Boop.. I don't think I know this category (you can't update any% yet) :p");
        }

//        check map for valid
        int mapID = GlobalThings.getMapIDS().indexOf(mapName);
        if (mapID == -1) {
            return event.reply()
                    .withEphemeral(true)
                    .withContent("Beep Boop.. This map doesn't exist or wasn't added yet :p");
        }

//        convert time
        try{
            mapTime = RecordFormatter.StringToNumber(timeOption);
        } catch (Exception e) {
            return event.reply()
                    .withEphemeral(true)
                    .withContent("Beep Boop.. Something wrong with your record time format :p");
        }

        MapRecord existingRecord;
        try{
            existingRecord = mapRecordService.getRecord(mapID, category);
        } catch (Exception e) {
            existingRecord = null;
        }

        if (existingRecord!=null){
            if (existingRecord.getCurr_wr_seconds() <= mapTime){
                return event.reply()
                        .withEphemeral(true)
                        .withContent("Beep Boop.. Current WR is either same or better :p");
            }
        }



//        ?check img if valid?

//        ?check img 2 if valid?

//        ?check img 3 if valid?

//        ?check vid if valid?

//       split stage times
        if (stageTimesOption != null){
            String[] stageTimeStrings = stageTimesOption.split(", ");
            if (stageTimeStrings.length != 3){
                event.reply()
                        .withEphemeral(true)
                        .withContent("Beep Boop.. Couldn't split the stage times correctly, I'll ignore those :p");
            } else {
                stage_1_time = RecordFormatter.StringToNumber(stageTimeStrings[0]);
                stage_2_time = RecordFormatter.StringToNumber(stageTimeStrings[1]);
                stage_3_time = RecordFormatter.StringToNumber(stageTimeStrings[2]);

                if (stage_1_time + stage_2_time + stage_3_time !=mapTime){
                    event.reply()
                            .withEphemeral(true)
                            .withContent("Beep Boop.. Stage times don't add up to total time, I'll ignore those :p");
                }
            }
        }

        MapRecord newRecord;

        if (category.equals("solo")){
            newRecord = new SoloMapRecord();
        } else {
            return event.reply()
                    .withEphemeral(true)
                    .withContent("Beep Boop.. Can't create new record, something's wrong with the category :p");
        }

        newRecord.setId(mapID);
        newRecord.setMap_name(mapName);
        if (existingRecord != null){
            newRecord.setPrev_wr_seconds(existingRecord.getCurr_wr_seconds());    
        }
        
        newRecord.setCurr_wr_seconds((short) mapTime);
        newRecord.setProof_img_1_link(img1_link);
        newRecord.setProof_img_2_link(img2_link);
        newRecord.setProof_img_3_link(img3_link);
        newRecord.setProof_vid_link(vid_link);
        if (stage_1_time != 0){
            newRecord.setStage_1_time_seconds((short) stage_1_time);
            newRecord.setStage_2_time_seconds((short) stage_2_time);
            newRecord.setStage_3_time_seconds((short) stage_3_time);
        }

        mapRecordService.addRecord(newRecord);

        return event.reply()
                .withEphemeral(false)
                .withContent(mapName + " WR for category " + category + "% Updated! \n" +
                        "new  WR set -> " + timeOption + "\n" +
                        newRecord.getProof_img_1_link() + "\n" +
                        "I think it's updated.. check if it updated now");
    }
}
