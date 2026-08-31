package com.thousand_uncles.data.service;

import com.thousand_uncles.data.models.common.ConfirmWorthyMapRecordEntry;
import com.thousand_uncles.data.models.common.ManualIndexedMapRecordEntry;
import com.thousand_uncles.data.models.run.RunAnyPercentMapRecordEntry;
import com.thousand_uncles.data.models.run.RunSoloMapRecordEntry;
import com.thousand_uncles.data.models.run.TestRecord;
import com.thousand_uncles.data.models.uncletopia.AnyPercentMapRecordEntry;
import com.thousand_uncles.data.models.uncletopia.SoloMapRecordEntry;
import com.thousand_uncles.data.repositories.run.RunAnyPercentMapRecordRepository;
import com.thousand_uncles.data.repositories.run.RunCheeselessMapRecordRepository;
import com.thousand_uncles.data.repositories.run.RunSoloMapRecordRepository;
import com.thousand_uncles.data.repositories.uncletopia.AnyPercentMapRecordRepository;
import com.thousand_uncles.data.repositories.uncletopia.ConfirmWorthyRepository;
import com.thousand_uncles.data.repositories.uncletopia.SoloMapRecordRepository;
import com.thousand_uncles.data.repositories.TestMapRecordRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional
@Profile("prod")
public class MapRecordServiceProd {
    @SuppressWarnings("unused")
    @Autowired
    private SoloMapRecordRepository soloMapRecordRepository;

    @SuppressWarnings("unused")
    @Autowired
    private AnyPercentMapRecordRepository anyPercentMapRecordRepository;

    @Autowired
    private RunAnyPercentMapRecordRepository runAnyPercentMapRecordRepository;

    @Autowired
    private RunSoloMapRecordRepository runSoloMapRecordRepository;

    @Autowired
    RunCheeselessMapRecordRepository runCheeselessMapRecordRepository;

    @SuppressWarnings("unused")
    @Autowired
    private ConfirmWorthyRepository confirmWorthyRepository;

    @SuppressWarnings("unused")
    @Autowired
    private TestMapRecordRepository testMapRecordRepository;

    @SuppressWarnings("unused")
    @PersistenceContext
    private EntityManager entityManager;

    MapRecordServiceProd(){
        System.out.println("maprecordserviceprod initialized");
    }



    // Add new record
    @SuppressWarnings("unused")
    public ManualIndexedMapRecordEntry addRecord(Object record) {
        if (record instanceof SoloMapRecordEntry soloMapRecord){
            SoloMapRecordEntry soloMapRecordEntry = new SoloMapRecordEntry(
                    soloMapRecord.getId(),
                    soloMapRecord.getMap_name(),
                    soloMapRecord.getThe_hero(),
                    soloMapRecord.getCurr_wr_seconds(),
                    BigDecimal.ZERO,
                    soloMapRecord.getProof_img_1_link(),
                    soloMapRecord.getProof_img_2_link(),
                    soloMapRecord.getProof_img_3_link(),
                    soloMapRecord.getProof_vid_link(),
                    soloMapRecord.getStage_1_time_seconds(),
                    soloMapRecord.getStage_2_time_seconds(),
                    soloMapRecord.getStage_3_time_seconds()
                    );

            return soloMapRecordRepository.save(soloMapRecordEntry);
        } else if (record instanceof AnyPercentMapRecordEntry anyPercentMapRecord) {
            AnyPercentMapRecordEntry anyPercentMapRecordEntry = new AnyPercentMapRecordEntry(
                    anyPercentMapRecord.getId(),
                    anyPercentMapRecord.getMap_name(),
                    anyPercentMapRecord.getCurr_wr_seconds(),
                    BigDecimal.ZERO,
                    anyPercentMapRecord.getProof_img_1_link(),
                    anyPercentMapRecord.getProof_img_2_link(),
                    anyPercentMapRecord.getProof_img_3_link(),
                    anyPercentMapRecord.getProof_vid_link(),
                    anyPercentMapRecord.getStage_1_time_seconds(),
                    anyPercentMapRecord.getStage_2_time_seconds(),
                    anyPercentMapRecord.getStage_3_time_seconds()
            );
            return anyPercentMapRecordRepository.save((AnyPercentMapRecordEntry) record);
        }
        return null;
    }

    @SuppressWarnings("unused")
    public ManualIndexedMapRecordEntry getRecord(int ID, String category){
        ManualIndexedMapRecordEntry foundMap = null;
        switch (category){
            case "solo":
                foundMap = soloMapRecordRepository.findById(ID).orElse(null);
                break;
            case "any":
                foundMap = anyPercentMapRecordRepository.findById(ID).orElse(null);
                break;
            case "cheeseless":
                break;
            case "run_any":
                foundMap = runAnyPercentMapRecordRepository.findById(ID).orElse(null);
                break;
            case "run_solo":
                foundMap = runSoloMapRecordRepository.findById(ID).orElse(null);
                break;
            case "run_cheeseless":
                foundMap = runCheeselessMapRecordRepository.findById(ID).orElse(null);
                break;
        }
        return foundMap;
    }

//    Modify existing WR
    @SuppressWarnings("unused")
    public boolean updateWR(ManualIndexedMapRecordEntry record){
        int updated = 0;
        if (record instanceof SoloMapRecordEntry){
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
        } else if (record instanceof AnyPercentMapRecordEntry) {
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
    public List<ManualIndexedMapRecordEntry> searchRecords(String partialName, String category) {
        return switch (category) {
            case "solo" -> soloMapRecordRepository.findByMap_nameContaining(partialName);
            case "any" -> anyPercentMapRecordRepository.findByMap_nameContaining(partialName);
            case "run_solo" -> runSoloMapRecordRepository.findByMap_nameContaining(partialName);
            case "run_any" -> runAnyPercentMapRecordRepository.findByMap_nameContaining(partialName);
            default -> null;
        };
    }

    @SuppressWarnings("unused")
    public ManualIndexedMapRecordEntry saveUncletopiaSolo(
            int ID,
            String map_name,
            BigDecimal curr_wr_time,
            BigDecimal prev_wr_time,
            String the_hero,
            String proof_1_link,
            String proof_2_link,
            String proof_3_link,
            String proof_vid_link,
            BigDecimal stage_1_time,
            BigDecimal stage_2_time,
            BigDecimal stage_3_time
    ){
        SoloMapRecordEntry recordToSave = new SoloMapRecordEntry(
                ID,
                map_name,
                the_hero,
                curr_wr_time,
                prev_wr_time,
                proof_1_link,
                proof_2_link,
                proof_3_link,
                proof_vid_link,
                stage_1_time,
                stage_2_time,
                stage_3_time
        );

        return soloMapRecordRepository.save(recordToSave);
    }

    @SuppressWarnings("unused")
    public ManualIndexedMapRecordEntry saveUncletopiaAny(
            int ID,
            String map_name,
            BigDecimal curr_wr_time,
            BigDecimal prev_wr_time,
            String proof_1_link,
            String proof_2_link,
            String proof_3_link,
            String proof_vid_link,
            BigDecimal stage_1_time,
            BigDecimal stage_2_time,
            BigDecimal stage_3_time
    ){
        AnyPercentMapRecordEntry recordToSave = new AnyPercentMapRecordEntry(
                ID,
                map_name,
                curr_wr_time,
                prev_wr_time,
                proof_1_link,
                proof_2_link,
                proof_3_link,
                proof_vid_link,
                stage_1_time,
                stage_2_time,
                stage_3_time
        );

        return anyPercentMapRecordRepository.save(recordToSave);
    }

    @SuppressWarnings("unused")
    public ManualIndexedMapRecordEntry saveRunSolo(
            int ID,
            String map_name,
            BigDecimal curr_wr_time,
            BigDecimal prev_wr_time,
            String the_hero,
            String proof_1_link,
            String proof_2_link,
            String proof_3_link,
            String proof_vid_link,
            BigDecimal stage_1_time,
            BigDecimal stage_2_time,
            BigDecimal stage_3_time
    ){
        RunSoloMapRecordEntry recordToSave = new RunSoloMapRecordEntry(
                ID,
                map_name,
                the_hero,
                curr_wr_time,
                prev_wr_time,
                proof_1_link,
                proof_2_link,
                proof_3_link,
                proof_vid_link,
                stage_1_time,
                stage_2_time,
                stage_3_time
        );

        return runSoloMapRecordRepository.save(recordToSave);
    }

    @SuppressWarnings("unused")
    public ManualIndexedMapRecordEntry saveRunAny(
            int ID,
            String map_name,
            BigDecimal curr_wr_time,
            BigDecimal prev_wr_time,
            String proof_1_link,
            String proof_2_link,
            String proof_3_link,
            String proof_vid_link,
            BigDecimal stage_1_time,
            BigDecimal stage_2_time,
            BigDecimal stage_3_time
    ){
        RunAnyPercentMapRecordEntry recordToSave = new RunAnyPercentMapRecordEntry(
                ID,
                map_name,
                curr_wr_time,
                prev_wr_time,
                proof_1_link,
                proof_2_link,
                proof_3_link,
                proof_vid_link,
                stage_1_time,
                stage_2_time,
                stage_3_time
        );

        return runAnyPercentMapRecordRepository.save(recordToSave);
    }

    public ConfirmWorthyMapRecordEntry putOnHold(
            String category,
            int ID,
            String map_name,
            BigDecimal curr_wr_time,
            BigDecimal prev_wr_time,
            String proof_1_link,
            String proof_2_link,
            String proof_3_link,
            String proof_vid_link,
            BigDecimal stage_1_time,
            BigDecimal stage_2_time,
            BigDecimal stage_3_time,
            String additional
    ) {
        ConfirmWorthyMapRecordEntry recordToUpdate = new ConfirmWorthyMapRecordEntry();
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

    public ConfirmWorthyMapRecordEntry getFromHold(
            String category,
            int map_id
    ){
        ConfirmWorthyMapRecordEntry foundRecord = confirmWorthyRepository.findMapByCategory(map_id, category);
        confirmWorthyRepository.delete(foundRecord);
        return foundRecord;
    }

    public TestRecord addTestRecord(
            int ID,
            String map_name,
            BigDecimal curr_wr_time,
            BigDecimal prev_wr_time,
            String proof_1_link,
            String proof_2_link,
            String proof_3_link,
            String proof_vid_link,
            BigDecimal stage_1_time,
            BigDecimal stage_2_time,
            BigDecimal stage_3_time
    ) {
        System.out.println("writing curr WR: " + curr_wr_time);

        TestRecord testRecord = new TestRecord(
                ID,
                map_name,
                curr_wr_time,
                prev_wr_time,
                proof_1_link,
                proof_2_link,
                proof_3_link,
                proof_vid_link,
                stage_1_time,
                stage_2_time,
                stage_3_time
        );

        return testMapRecordRepository.save(testRecord);
    }

    public TestRecord getTestRecord(int ID){
        return testMapRecordRepository.findById(ID).orElse(null);
    }
}