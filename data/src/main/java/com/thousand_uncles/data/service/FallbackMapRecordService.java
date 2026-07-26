package com.thousand_uncles.data.service;

import com.thousand_uncles.data.models.MapRecord;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Profile("dev")
public class FallbackMapRecordService {

    FallbackMapRecordService(){
        System.out.println("Fallback record service initialized");
    }
    @SuppressWarnings("unused")
    public void addRecord(Object record) {
    }

    // Read record by name
    @SuppressWarnings("unused")
    public MapRecord getRecordByName(String mapName) {
        return null;
    }

    // Read all records
    @SuppressWarnings("unused")
    public List<MapRecord> getAllRecords() {
        return null;
    }

    // Update record by name
    @SuppressWarnings("unused")
    public boolean updateMapName(String oldName, String newName) {
//        int updated = com.thousand_uncles.data.repository.updateMap_name(oldName, newName);
        return false;
    }

    // Delete record by name
    @SuppressWarnings("unused")
    public boolean deleteRecord(String mapName) {
//        int deleted = com.thousand_uncles.data.repository.deleteByMap_name(mapName);
        return false;
    }

    //    Modify existing WR
    @SuppressWarnings("unused")
    public boolean updateWR(
            String map_name,
            Short curr_wr_seconds,
            String proof_pic_1_link,
            String proof_pic_2_link,
            String proof_pic_3_link,
            String proof_vid_link,
            Short stage_time_1,
            Short stage_time_2,
            Short stage_time_3
    ){
        return false;
    }

    // Search records by partial name
    @SuppressWarnings("unused")
    public List<MapRecord> searchRecords(String partialName) {
        return null;
    }
}
