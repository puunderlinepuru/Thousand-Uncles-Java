package com.thousand_uncles.google_api_handler.data.service;

import com.thousand_uncles.google_api_handler.data.models.AnyPercentMapRecord;
import com.thousand_uncles.google_api_handler.data.models.MapRecord;
import com.thousand_uncles.google_api_handler.data.models.SoloMapRecord;
import com.thousand_uncles.google_api_handler.data.repository.AnyPercentMapRecordRepository;
import com.thousand_uncles.google_api_handler.data.repository.SoloMapRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@Transactional
@Profile("prod")
public class MapRecordService {

    @SuppressWarnings("unused")
    @Autowired(required = false)
    private SoloMapRecordRepository soloMapRecordRepository;

    @SuppressWarnings("unused")
    @Autowired(required = false)
    private AnyPercentMapRecordRepository anyPercentMapRecordRepository;

    // Add new record
    @SuppressWarnings("unused")
    public void addRecord(Object record) {
        if (record instanceof SoloMapRecord){
            soloMapRecordRepository.save((SoloMapRecord) record);
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

    // Read record by name
    /*@SuppressWarnings("unused")
    public MapRecord getRecordByName(String mapName) {
        return anyPercentMapRecordRepository.findByMap_name(mapName);
    }*/

    // Read all records
    /*@SuppressWarnings("unused")
    public List<MapRecord> getAllRecords() {
        return repository.findAll();
    }*/

    // Update record by name
    /*@SuppressWarnings("unused")
    public boolean updateMapName(String oldName, String newName) {
        int updated = repository.updateMap_name(oldName, newName);
        return updated > 0;
    }*/

    // Delete record by name
    /*@SuppressWarnings("unused")
    public boolean deleteRecord(Object record) {
        int deleted = repository.deleteByMap_name(mapName);
        return deleted > 0;
    }*/

    //    Modify existing WR
    @SuppressWarnings("unused")
    public boolean updateWR( MapRecord record){
        int updated = 0;
        if (record instanceof SoloMapRecord){
            updated = soloMapRecordRepository.updateRecord(
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
}