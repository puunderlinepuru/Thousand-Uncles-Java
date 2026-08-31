package com.thousand_uncles.discord_bot.bot.commands;

import com.thousand_uncles.data.models.uncletopia.AnyPercentMapRecordEntry;
import com.thousand_uncles.data.models.common.ConfirmWorthyMapRecordEntry;
import com.thousand_uncles.data.models.common.ManualIndexedMapRecordEntry;
import com.thousand_uncles.data.service.MapRecordServiceProd;
import com.thousand_uncles.data.util.RecordFormatter;
import com.thousand_uncles.discord_bot.common.config.BotConfig;
import com.thousand_uncles.discord_bot.common.util.GlobalThings;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.object.command.ApplicationCommandInteractionOption;
import discord4j.core.object.command.ApplicationCommandInteractionOptionValue;
import discord4j.core.object.component.ActionRow;
import discord4j.core.object.component.Button;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

@SuppressWarnings("unused")
@Component
public class UpdateAnyCommand implements SlashCommand{

    @Autowired
    BotConfig botConfig;

    @SuppressWarnings("unused")
    @Autowired
    ApplicationContext applicationContext;

    @Override
    public String getName() {
        return "update_any";
    }

    @Override
    public Mono<Void> handle(ChatInputInteractionEvent event){
        MapRecordServiceProd mapRecordServiceProd = applicationContext.getBean(MapRecordServiceProd.class);

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

        BigDecimal mapTime, stage_1_time = BigDecimal.ZERO, stage_2_time = BigDecimal.ZERO, stage_3_time = BigDecimal.ZERO;

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
            mapTime = RecordFormatter.StringToBigDecimal(timeOption);
        } catch (Exception e) {
            return event.reply()
                    .withEphemeral(true)
                    .withContent("Beep Boop.. Something wrong with your record time format :p");
        }

        ManualIndexedMapRecordEntry existingRecord;
        try{
            existingRecord = mapRecordServiceProd.getRecord(mapID, "any");
        } catch (Exception e) {
            existingRecord = null;
        }

        if (existingRecord!=null){
            if (existingRecord.getCurr_wr_seconds().compareTo(mapTime) <= 0){
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
                stage_1_time = RecordFormatter.StringToBigDecimal(stageTimeStrings[0]);
                stage_2_time = RecordFormatter.StringToBigDecimal(stageTimeStrings[1]);
                stage_3_time = RecordFormatter.StringToBigDecimal(stageTimeStrings[2]);

                if (stage_1_time.add(stage_2_time).add(stage_3_time).compareTo(mapTime) != 0){
                    event.reply()
                            .withEphemeral(true)
                            .withContent("Beep Boop.. Stage times don't add up to total time, I'll ignore those :p");
                }
            }
        }

        AnyPercentMapRecordEntry newRecord = new AnyPercentMapRecordEntry();

        newRecord.setId(mapID);
        newRecord.setMap_name(mapName);
        if (existingRecord != null){
            newRecord.setPrev_wr_seconds(existingRecord.getCurr_wr_seconds());    
        }
        
        newRecord.setCurr_wr_seconds(mapTime);
        newRecord.setProof_img_1_link(img1_link);
        newRecord.setProof_img_2_link(img2_link);
        newRecord.setProof_img_3_link(img3_link);
        newRecord.setProof_vid_link(vid_link);
        if (stage_1_time.compareTo(BigDecimal.ZERO) != 0){
            newRecord.setStage_1_time_seconds(stage_1_time);
            newRecord.setStage_2_time_seconds(stage_2_time);
            newRecord.setStage_3_time_seconds(stage_3_time);
        }

//        mapRecordServiceProd.addRecord(newRecord);
        ConfirmWorthyMapRecordEntry savedRecord = mapRecordServiceProd.putOnHold(
                "any",
                mapID,
                mapName,
                mapTime,
                BigDecimal.ZERO,
                img1_link,
                img2_link,
                img3_link,
                vid_link,
                stage_1_time,
                stage_2_time,
                stage_3_time,
                "");

        return event.reply()
                .withEphemeral(false)
                .withContent(mapName + " WR for category Any% Updated! \n" +
                        "new  WR set -> " + timeOption + "\n" +
                        newRecord.getProof_img_1_link() + "\n" +
                        "Record noted down, waiting for approval :p")
                .withComponents(ActionRow.of(
                        Button.primary("approve-any-" + mapName + "-" + mapTime, "Validate (for admis)")
                ));
    }
}
