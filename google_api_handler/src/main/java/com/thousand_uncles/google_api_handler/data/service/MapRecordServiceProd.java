package com.thousand_uncles.google_api_handler.data.service;

import com.thousand_uncles.google_api_handler.data.repository.AnyPercentMapRecordRepository;
import com.thousand_uncles.google_api_handler.data.repository.SoloMapRecordRepository;
import com.thousand_uncles.google_api_handler.data.models.AnyPercentMapRecord;
import com.thousand_uncles.google_api_handler.data.models.MapRecord;
import com.thousand_uncles.google_api_handler.data.models.SoloMapRecord;
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

    public MapRecordServiceProd(){
        System.out.println("maprecordserviceprod initialized");
    }

    @SuppressWarnings("unused")
    @Autowired(required = false)
    private SoloMapRecordRepository soloMapRecordRepository;

    @SuppressWarnings("unused")
    @Autowired(required = false)
    private AnyPercentMapRecordRepository anyPercentMapRecordRepository;

    @SuppressWarnings("unused")
    @PersistenceContext
    private EntityManager entityManager;

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

    // Read record by name
    /*@SuppressWarnings("unused")
    public MapRecord getRecordByName(String mapName) {
        return anyPercentMapRecordRepository.findByMap_name(mapName);
    }*/

    // Read all records
    /*@SuppressWarnings("unused")
    public List<MapRecord> getAllRecords() {
        return com.thousand_uncles.data.repository.findAll();
    }*/

    // Update record by name
    /*@SuppressWarnings("unused")
    public boolean updateMapName(String oldName, String newName) {
        int updated = com.thousand_uncles.data.repository.updateMap_name(oldName, newName);
        return updated > 0;
    }*/

    // Delete record by name
    /*@SuppressWarnings("unused")
    public boolean deleteRecord(Object record) {
        int deleted = com.thousand_uncles.data.repository.deleteByMap_name(mapName);
        return deleted > 0;
    }*/

    // Search records by partial name
    @SuppressWarnings("unused")
    public List<MapRecord> searchRecords(String partialName, String category) {
        return switch (category) {
            case "solo" -> soloMapRecordRepository.findByMap_nameContaining(partialName);
            case "any" -> anyPercentMapRecordRepository.findByMap_nameContaining(partialName);
            default -> null;
        };
    }

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
}