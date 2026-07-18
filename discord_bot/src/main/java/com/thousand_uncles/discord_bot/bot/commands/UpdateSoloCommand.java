package com.thousand_uncles.discord_bot.bot.commands;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.thousand_uncles.discord_bot.bot.config.BotConfig;
import com.thousand_uncles.discord_bot.bot.util.GlobalThings;
import com.thousand_uncles.discord_bot.data.models.AnyPercentMapRecord;
import com.thousand_uncles.discord_bot.data.models.ConfirmWorthyMapRecord;
import com.thousand_uncles.discord_bot.data.models.MapRecord;
import com.thousand_uncles.discord_bot.data.service.MapRecordServiceProd;
import com.thousand_uncles.discord_bot.data.util.RecordFormatter;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.object.command.ApplicationCommandInteractionOption;
import discord4j.core.object.command.ApplicationCommandInteractionOptionValue;
import discord4j.core.object.component.ActionRow;
import discord4j.core.object.component.Button;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@SuppressWarnings("unused")
@Component
public class UpdateSoloCommand implements SlashCommand{

    @Autowired
    BotConfig botConfig;

    @SuppressWarnings("unused")
    @Autowired
    ApplicationContext applicationContext;

    @Override
    public String getName() {
        return "update_solo";
    }

    @Override
    public Mono<Void> handle(ChatInputInteractionEvent event){
        MapRecordServiceProd mapRecordServiceProd = applicationContext.getBean(MapRecordServiceProd.class);

        String theHero = event.getOption("the_hero")
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

        int mapTime, stage_1_time = 0, stage_2_time = 0, stage_3_time = 0;

//        check map for valid
        int mapID = GlobalThings.getMapIDS().indexOf(mapName);
        if (mapID == -1) {
            return event.reply()
                    .withEphemeral(true)
                    .withContent("Beep Boop.. This map doesn't exist or wasn't added yet :p");
        }

//        convert time
        try{
            assert timeOption != null;
            mapTime = RecordFormatter.StringToNumber(timeOption);
        } catch (Exception e) {
            return event.reply()
                    .withEphemeral(true)
                    .withContent("Beep Boop.. Something wrong with your record time format :p");
        }

        MapRecord existingRecord;
        try{
            existingRecord = mapRecordServiceProd.getRecord(mapID, "solo");
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

//        THE HERO
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode theHeroNode = objectMapper.createObjectNode();
        theHeroNode.put("the_hero", theHero);
        String theHeroString;
        try {
            theHeroString = objectMapper.writeValueAsString(theHeroNode);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
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

        AnyPercentMapRecord newRecord = new AnyPercentMapRecord();

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

//        mapRecordServiceProd.addRecord(newRecord);
        ConfirmWorthyMapRecord savedRecord = mapRecordServiceProd.putOnHold(
                "solo",
                mapID,
                mapName,
                (short) mapTime,
                (short) 0,
                img1_link,
                img2_link,
                img3_link,
                vid_link,
                (short) stage_1_time,
                (short) stage_2_time,
                (short) stage_3_time,
                theHeroString);

        return event.reply()
                .withEphemeral(false)
                .withContent(mapName + " WR for category Solo% Updated! \n" +
                        "new  WR set -> " + timeOption + "\n" +
                        "**The Hero:** " + theHero + "\n" +
                        newRecord.getProof_img_1_link() + "\n" +
                        "Record noted down, waiting for approval :p")
                .withComponents(ActionRow.of(
                        Button.primary("approve-solo-" + mapName + "-" + mapTime, "Validate (for admis)")
                ));
    }
}
