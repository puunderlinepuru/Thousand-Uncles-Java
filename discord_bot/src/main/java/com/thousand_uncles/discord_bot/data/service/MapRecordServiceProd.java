package com.thousand_uncles.discord_bot.data.service;

import com.thousand_uncles.discord_bot.data.models.AnyPercentMapRecord;
import com.thousand_uncles.discord_bot.data.models.ConfirmWorthyMapRecord;
import com.thousand_uncles.discord_bot.data.models.MapRecord;
import com.thousand_uncles.discord_bot.data.models.SoloMapRecord;
import com.thousand_uncles.discord_bot.data.repository.AnyPercentMapRecordRepository;
import com.thousand_uncles.discord_bot.data.repository.ConfirmWorthyRepository;
import com.thousand_uncles.discord_bot.data.repository.SoloMapRecordRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

@Service
@Transactional
@Profile("prod")
public class MapRecordServiceProd {

    MapRecordServiceProd(){
        System.out.println("maprecordserviceprod initialized");
    }

    @SuppressWarnings("unused")
    @Autowired(required = false)
    private SoloMapRecordRepository soloMapRecordRepository;

    @SuppressWarnings("unused")
    @Autowired(required = false)
    private AnyPercentMapRecordRepository anyPercentMapRecordRepository;

    @SuppressWarnings("unused")
    @Autowired(required = false)
    private ConfirmWorthyRepository confirmWorthyRepository;

    @SuppressWarnings("unused")
    @PersistenceContext
    private EntityManager entityManager;

    // Add new record
    @SuppressWarnings("unused")
    public void addRecord(Object record) {
        if (record instanceof SoloMapRecord soloMapRecord){
            entityManager.joinTransaction();
            soloMapRecordRepository.upsert(
                    soloMapRecord.getId(),
                    soloMapRecord.getMap_name(),
                    soloMapRecord.getCurr_wr_seconds(),
                    (short) 0,
                    soloMapRecord.getProof_img_1_link(),
                    soloMapRecord.getProof_img_2_link(),
                    soloMapRecord.getProof_img_3_link(),
                    soloMapRecord.getProof_vid_link(),
                    soloMapRecord.getStage_1_time_seconds(),
                    soloMapRecord.getStage_2_time_seconds(),
                    soloMapRecord.getStage_3_time_seconds()
            );
        } else if (record instanceof AnyPercentMapRecord) {
            anyPercentMapRecordRepository.save((AnyPercentMapRecord) record);
        }
    }

    @SuppressWarnings("unused")
    public MapRecord getRecord(int ID, String category){
        MapRecord foundMap = null;
        if(Objects.equals(category, "solo")){
            foundMap = soloMapRecordRepository.findById(ID).orElseThrow();
        } else if (Objects.equals(category, "any")){
            foundMap = anyPercentMapRecordRepository.findById(ID).orElseThrow();
        }
        return foundMap;
    }

//    Modify existing WR
    @SuppressWarnings("unused")
    public boolean updateWR(MapRecord record){
        int updated = 0;
        if (record instanceof SoloMapRecord){
            updated = soloMapRecordRepository.updateRecordByName(
                    record.getMap_name(),
                    record.getCurr_wr_seconds(),
                    record.getProof_img_1_link(),
                    record.getProof_img_2_link(),
                    record.getProof_img_3_link(),
                    record.getProof_vid_link(),
                    record.getStage_1_time_seconds(),
                    record.getStage_2_time_seconds(),
                    record.getStage_3_time_seconds()
            );
        } else if (record instanceof AnyPercentMapRecord) {
            updated = anyPercentMapRecordRepository.updateRecord(
                    record.getMap_name(),
                    record.getCurr_wr_seconds(),
                    record.getProof_img_1_link(),
                    record.getProof_img_2_link(),
                    record.getProof_img_3_link(),
                    record.getProof_vid_link(),
                    record.getStage_1_time_seconds(),
                    record.getStage_2_time_seconds(),
                    record.getStage_3_time_seconds()
            );
        }
        return updated > 0;
    }

    // Search records by partial name
    @SuppressWarnings("unused")
    public List<MapRecord> searchRecords(String partialName, String category) {
        return switch (category) {
            case "solo" -> soloMapRecordRepository.findByMap_nameContaining(partialName);
            case "any" -> anyPercentMapRecordRepository.findByMap_nameContaining(partialName);
            default -> null;
        };
    }

    @SuppressWarnings("unused")
    public MapRecord saveSolo(
            int ID,
            String map_name,
            short curr_wr_time,
            short prev_wr_time,
            String the_hero,
            String proof_1_link,
            String proof_2_link,
            String proof_3_link,
            String proof_vid_link,
            Short stage_1_time,
            Short stage_2_time,
            Short stage_3_time
    ){
        SoloMapRecord recordToSave = new SoloMapRecord();
        recordToSave.setId(ID);
        recordToSave.setMap_name(map_name);
        recordToSave.setCurr_wr_seconds(curr_wr_time);
        recordToSave.setPrev_wr_seconds(prev_wr_time);
        recordToSave.setThe_hero(the_hero);
        recordToSave.setProof_img_1_link(proof_1_link);
        recordToSave.setProof_img_2_link(proof_2_link);
        recordToSave.setProof_img_3_link(proof_3_link);
        recordToSave.setProof_vid_link(proof_vid_link);
        recordToSave.setStage_1_time_seconds(stage_1_time);
        recordToSave.setStage_2_time_seconds(stage_2_time);
        recordToSave.setStage_3_time_seconds(stage_3_time);

        return soloMapRecordRepository.save(recordToSave);
    }

    @SuppressWarnings("unused")
    public MapRecord saveAny(
            int ID,
            String map_name,
            short curr_wr_time,
            short prev_wr_time,
            String proof_1_link,
            String proof_2_link,
            String proof_3_link,
            String proof_vid_link,
            Short stage_1_time,
            Short stage_2_time,
            Short stage_3_time
    ){
        AnyPercentMapRecord recordToSave = new AnyPercentMapRecord();
        recordToSave.setId(ID);
        recordToSave.setMap_name(map_name);
        recordToSave.setCurr_wr_seconds(curr_wr_time);
        recordToSave.setPrev_wr_seconds(prev_wr_time);
        recordToSave.setProof_img_1_link(proof_1_link);
        recordToSave.setProof_img_2_link(proof_2_link);
        recordToSave.setProof_img_3_link(proof_3_link);
        recordToSave.setProof_vid_link(proof_vid_link);
        recordToSave.setStage_1_time_seconds(stage_1_time);
        recordToSave.setStage_2_time_seconds(stage_2_time);
        recordToSave.setStage_3_time_seconds(stage_3_time);

        return anyPercentMapRecordRepository.save(recordToSave);
    }

    @SuppressWarnings("unused")
    public MapRecord updateSolo(
            int ID,
            String map_name,
            short curr_wr_time,
            short prev_wr_time,
            String the_hero,
            String proof_1_link,
            String proof_2_link,
            String proof_3_link,
            String proof_vid_link,
            Short stage_1_time,
            Short stage_2_time,
            Short stage_3_time
    )  throws NoSuchElementException {
        SoloMapRecord recordToUpdate = soloMapRecordRepository.findById(ID).orElseThrow();
        recordToUpdate.setId(ID);
        recordToUpdate.setMap_name(map_name);
        recordToUpdate.setCurr_wr_seconds(curr_wr_time);
        recordToUpdate.setPrev_wr_seconds(prev_wr_time);
        recordToUpdate.setThe_hero(the_hero);
        recordToUpdate.setProof_img_1_link(proof_1_link);
        recordToUpdate.setProof_img_2_link(proof_2_link);
        recordToUpdate.setProof_img_3_link(proof_3_link);
        recordToUpdate.setProof_vid_link(proof_vid_link);
        recordToUpdate.setStage_1_time_seconds(stage_1_time);
        recordToUpdate.setStage_2_time_seconds(stage_2_time);
        recordToUpdate.setStage_3_time_seconds(stage_3_time);

        return soloMapRecordRepository.save(recordToUpdate);
    }

    @SuppressWarnings("unused")
    public MapRecord updateAny(
            int ID,
            String map_name,
            short curr_wr_time,
            short prev_wr_time,
            String proof_1_link,
            String proof_2_link,
            String proof_3_link,
            String proof_vid_link,
            Short stage_1_time,
            Short stage_2_time,
            Short stage_3_time
    )  throws NoSuchElementException {
        AnyPercentMapRecord recordToUpdate = anyPercentMapRecordRepository.findById(ID).orElseThrow();
        recordToUpdate.setId(ID);
        recordToUpdate.setMap_name(map_name);
        recordToUpdate.setCurr_wr_seconds(curr_wr_time);
        recordToUpdate.setPrev_wr_seconds(prev_wr_time);
        recordToUpdate.setProof_img_1_link(proof_1_link);
        recordToUpdate.setProof_img_2_link(proof_2_link);
        recordToUpdate.setProof_img_3_link(proof_3_link);
        recordToUpdate.setProof_vid_link(proof_vid_link);
        recordToUpdate.setStage_1_time_seconds(stage_1_time);
        recordToUpdate.setStage_2_time_seconds(stage_2_time);
        recordToUpdate.setStage_3_time_seconds(stage_3_time);

        return anyPercentMapRecordRepository.save(recordToUpdate);
    }


    public ConfirmWorthyMapRecord putOnHold(
            String category,
            int ID,
            String map_name,
            short curr_wr_time,
            short prev_wr_time,
            String proof_1_link,
            String proof_2_link,
            String proof_3_link,
            String proof_vid_link,
            Short stage_1_time,
            Short stage_2_time,
            Short stage_3_time,
            String additional
    ) {
        ConfirmWorthyMapRecord recordToUpdate = new ConfirmWorthyMapRecord();
        recordToUpdate.setCategory(category);
        recordToUpdate.setMap_id(ID);
        recordToUpdate.setMap_name(map_name);
        recordToUpdate.setCurr_wr_seconds(curr_wr_time);
        recordToUpdate.setPrev_wr_seconds(prev_wr_time);
        recordToUpdate.setProof_img_1_link(proof_1_link);
        recordToUpdate.setProof_img_2_link(proof_2_link);
        recordToUpdate.setProof_img_3_link(proof_3_link);
        recordToUpdate.setProof_vid_link(proof_vid_link);
        recordToUpdate.setStage_1_time_seconds(stage_1_time);
        recordToUpdate.setStage_2_time_seconds(stage_2_time);
        recordToUpdate.setStage_3_time_seconds(stage_3_time);
        recordToUpdate.setAdditional(additional);

        return confirmWorthyRepository.save(recordToUpdate);
    }

    @SuppressWarnings("unused")
    public void removeFromHold(
            String category,
            int map_id
    ){

    }

    public ConfirmWorthyMapRecord getFromHold(
            String category,
            int map_id
    ){
        ConfirmWorthyMapRecord foundRecord = confirmWorthyRepository.findMapByCategory(map_id, category);
        confirmWorthyRepository.delete(foundRecord);
        return foundRecord;
    }
}