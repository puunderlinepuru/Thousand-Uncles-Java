package com.thousand_uncles.data.service;

import com.thousand_uncles.data.models.common.ConfirmWorthyMapRecordEntry;
import com.thousand_uncles.data.models.common.ManualIndexedMapRecordEntry;
import com.thousand_uncles.data.models.run.RunAnyPercentMapRecordEntry;
import com.thousand_uncles.data.models.run.RunCheeselessMapRecordEntry;
import com.thousand_uncles.data.models.run.RunSoloMapRecordEntry;
import com.thousand_uncles.data.models.run.TestRecord;
import com.thousand_uncles.data.models.uncletopia.AnyPercentMapRecordEntry;
import com.thousand_uncles.data.models.uncletopia.CheeselessMapRecordEntry;
import com.thousand_uncles.data.models.uncletopia.SoloMapRecordEntry;
import com.thousand_uncles.data.repositories.run.RunAnyPercentMapRecordRepository;
import com.thousand_uncles.data.repositories.run.RunCheeselessMapRecordRepository;
import com.thousand_uncles.data.repositories.run.RunSoloMapRecordRepository;
import com.thousand_uncles.data.repositories.uncletopia.AnyPercentMapRecordRepository;
import com.thousand_uncles.data.repositories.uncletopia.CheeselessMapRecordRepository;
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

@Service
@Transactional
@Profile("online")
public class MapRecordServiceOnline {
    @Autowired
    private SoloMapRecordRepository soloMapRecordRepository;

    @Autowired
    private AnyPercentMapRecordRepository anyPercentMapRecordRepository;

    @Autowired
    private CheeselessMapRecordRepository cheeselessMapRecordRepository;

    @Autowired
    private RunAnyPercentMapRecordRepository runAnyPercentMapRecordRepository;

    @Autowired
    private RunSoloMapRecordRepository runSoloMapRecordRepository;

    @Autowired
    private RunCheeselessMapRecordRepository runCheeselessMapRecordRepository;

    @Autowired
    private ConfirmWorthyRepository confirmWorthyRepository;

    @Autowired
    private TestMapRecordRepository testMapRecordRepository;

    @PersistenceContext
    private EntityManager entityManager;

    MapRecordServiceOnline(){
        System.out.println("MapRecordServiceOnline initialized");
    }

    // Add new record
    @SuppressWarnings("unused")
    public ManualIndexedMapRecordEntry addRecord(Object record) {
        switch (record){
            case AnyPercentMapRecordEntry anyPercentMapRecordEntry:
                AnyPercentMapRecordEntry newAnyPercentMapEntry = new AnyPercentMapRecordEntry(
                        anyPercentMapRecordEntry.getId(),
                        anyPercentMapRecordEntry.getMap_name(),
                        anyPercentMapRecordEntry.getCurr_wr_seconds(),
                        BigDecimal.ZERO,
                        anyPercentMapRecordEntry.getProof_img_1_link(),
                        anyPercentMapRecordEntry.getProof_img_2_link(),
                        anyPercentMapRecordEntry.getProof_img_3_link(),
                        anyPercentMapRecordEntry.getProof_vid_link(),
                        anyPercentMapRecordEntry.getStage_1_time_seconds(),
                        anyPercentMapRecordEntry.getStage_2_time_seconds(),
                        anyPercentMapRecordEntry.getStage_3_time_seconds()
                );
                return anyPercentMapRecordRepository.save(newAnyPercentMapEntry);
            case SoloMapRecordEntry soloMapRecordEntry:
                SoloMapRecordEntry newSoloMapEntry = new SoloMapRecordEntry(
                        soloMapRecordEntry.getId(),
                        soloMapRecordEntry.getMap_name(),
                        soloMapRecordEntry.getThe_hero(),
                        soloMapRecordEntry.getCurr_wr_seconds(),
                        BigDecimal.ZERO,
                        soloMapRecordEntry.getProof_img_1_link(),
                        soloMapRecordEntry.getProof_img_2_link(),
                        soloMapRecordEntry.getProof_img_3_link(),
                        soloMapRecordEntry.getProof_vid_link(),
                        soloMapRecordEntry.getStage_1_time_seconds(),
                        soloMapRecordEntry.getStage_2_time_seconds(),
                        soloMapRecordEntry.getStage_3_time_seconds()
                );
                return soloMapRecordRepository.save(newSoloMapEntry);
            case CheeselessMapRecordEntry cheeselessMapRecordEntry:
                CheeselessMapRecordEntry newCheeselessMapEntry = new CheeselessMapRecordEntry(
                        cheeselessMapRecordEntry.getId(),
                        cheeselessMapRecordEntry.getMap_name(),
                        cheeselessMapRecordEntry.getCurr_wr_seconds(),
                        BigDecimal.ZERO,
                        cheeselessMapRecordEntry.getProof_img_1_link(),
                        cheeselessMapRecordEntry.getProof_img_2_link(),
                        cheeselessMapRecordEntry.getProof_img_3_link(),
                        cheeselessMapRecordEntry.getProof_vid_link(),
                        cheeselessMapRecordEntry.getStage_1_time_seconds(),
                        cheeselessMapRecordEntry.getStage_2_time_seconds(),
                        cheeselessMapRecordEntry.getStage_3_time_seconds()
                );
                return cheeselessMapRecordRepository.save(newCheeselessMapEntry);
            case RunAnyPercentMapRecordEntry runAnyPercentMapRecordEntry:
                RunAnyPercentMapRecordEntry newRunAnyPercentMapEntry = new RunAnyPercentMapRecordEntry(
                        runAnyPercentMapRecordEntry.getId(),
                        runAnyPercentMapRecordEntry.getMap_name(),
                        runAnyPercentMapRecordEntry.getCurr_wr_seconds(),
                        BigDecimal.ZERO,
                        runAnyPercentMapRecordEntry.getProof_img_1_link(),
                        runAnyPercentMapRecordEntry.getProof_img_2_link(),
                        runAnyPercentMapRecordEntry.getProof_img_3_link(),
                        runAnyPercentMapRecordEntry.getProof_vid_link(),
                        runAnyPercentMapRecordEntry.getStage_1_time_seconds(),
                        runAnyPercentMapRecordEntry.getStage_2_time_seconds(),
                        runAnyPercentMapRecordEntry.getStage_3_time_seconds()
                );
                return runAnyPercentMapRecordRepository.save(newRunAnyPercentMapEntry);
            case RunSoloMapRecordEntry runSoloMapRecordEntry:
                RunSoloMapRecordEntry newRunSoloMapEntry = new RunSoloMapRecordEntry(
                        runSoloMapRecordEntry.getId(),
                        runSoloMapRecordEntry.getMap_name(),
                        runSoloMapRecordEntry.getThe_hero(),
                        runSoloMapRecordEntry.getCurr_wr_seconds(),
                        BigDecimal.ZERO,
                        runSoloMapRecordEntry.getProof_img_1_link(),
                        runSoloMapRecordEntry.getProof_img_2_link(),
                        runSoloMapRecordEntry.getProof_img_3_link(),
                        runSoloMapRecordEntry.getProof_vid_link(),
                        runSoloMapRecordEntry.getStage_1_time_seconds(),
                        runSoloMapRecordEntry.getStage_2_time_seconds(),
                        runSoloMapRecordEntry.getStage_3_time_seconds()
                );
                return runSoloMapRecordRepository.save(newRunSoloMapEntry);
            case RunCheeselessMapRecordEntry runCheeselessMapRecordEntry:
                RunCheeselessMapRecordEntry newRunCheeselessMapEntry = new RunCheeselessMapRecordEntry(
                        runCheeselessMapRecordEntry.getId(),
                        runCheeselessMapRecordEntry.getMap_name(),
                        runCheeselessMapRecordEntry.getCurr_wr_seconds(),
                        BigDecimal.ZERO,
                        runCheeselessMapRecordEntry.getProof_img_1_link(),
                        runCheeselessMapRecordEntry.getProof_img_2_link(),
                        runCheeselessMapRecordEntry.getProof_img_3_link(),
                        runCheeselessMapRecordEntry.getProof_vid_link(),
                        runCheeselessMapRecordEntry.getStage_1_time_seconds(),
                        runCheeselessMapRecordEntry.getStage_2_time_seconds(),
                        runCheeselessMapRecordEntry.getStage_3_time_seconds()
                );
                return runCheeselessMapRecordRepository.save(newRunCheeselessMapEntry);
            default:
                throw new IllegalStateException("Unexpected value: " + record);
        }
    }

    public BigDecimal getCurrentWR(int id, String category){
        return switch (category) {
            case "any" -> anyPercentMapRecordRepository.getCurrWr(id);
            case "solo" -> soloMapRecordRepository.getCurrWr(id);
            case "cheeseless" -> cheeselessMapRecordRepository.getCurrWr(id);
            case "run_any" -> runAnyPercentMapRecordRepository.getCurrWr(id);
            case "run_solo" -> runSoloMapRecordRepository.getCurrWr(id);
            case "run_cheeseless" -> runCheeselessMapRecordRepository.getCurrWr(id);
            default -> null;
        };
    }

    public ManualIndexedMapRecordEntry updateRecord(Object record){
        BigDecimal brokenWR;
        switch (record){
            case AnyPercentMapRecordEntry anyPercentMapRecordEntry:
                AnyPercentMapRecordEntry existingAnyPercentMapRecordEntry = (AnyPercentMapRecordEntry) getRecord(anyPercentMapRecordEntry.getId(), "any");
                brokenWR = existingAnyPercentMapRecordEntry.getCurr_wr_seconds();
                existingAnyPercentMapRecordEntry.setMap_name(anyPercentMapRecordEntry.getMap_name());
                existingAnyPercentMapRecordEntry.setCurr_wr_seconds(anyPercentMapRecordEntry.getCurr_wr_seconds());
                existingAnyPercentMapRecordEntry.setPrev_wr_seconds(brokenWR);
                existingAnyPercentMapRecordEntry.setProof_img_1_link(anyPercentMapRecordEntry.getProof_img_1_link());
                existingAnyPercentMapRecordEntry.setProof_img_2_link(anyPercentMapRecordEntry.getProof_img_2_link());
                existingAnyPercentMapRecordEntry.setProof_img_3_link(anyPercentMapRecordEntry.getProof_img_3_link());
                existingAnyPercentMapRecordEntry.setProof_vid_link(anyPercentMapRecordEntry.getProof_vid_link());
                existingAnyPercentMapRecordEntry.setStage_1_time_seconds(anyPercentMapRecordEntry.getStage_1_time_seconds());
                existingAnyPercentMapRecordEntry.setStage_2_time_seconds(anyPercentMapRecordEntry.getStage_2_time_seconds());
                existingAnyPercentMapRecordEntry.setStage_3_time_seconds(anyPercentMapRecordEntry.getStage_3_time_seconds());
                return anyPercentMapRecordRepository.save(existingAnyPercentMapRecordEntry);
            case SoloMapRecordEntry soloMapRecordEntry:
                SoloMapRecordEntry existingSoloMapRecordEntry = (SoloMapRecordEntry) getRecord(soloMapRecordEntry.getId(), "solo");
                brokenWR = existingSoloMapRecordEntry.getCurr_wr_seconds();
                existingSoloMapRecordEntry.setMap_name(soloMapRecordEntry.getMap_name());
                existingSoloMapRecordEntry.setCurr_wr_seconds(soloMapRecordEntry.getCurr_wr_seconds());
                existingSoloMapRecordEntry.setPrev_wr_seconds(brokenWR);
                existingSoloMapRecordEntry.setProof_img_1_link(soloMapRecordEntry.getProof_img_1_link());
                existingSoloMapRecordEntry.setProof_img_2_link(soloMapRecordEntry.getProof_img_2_link());
                existingSoloMapRecordEntry.setProof_img_3_link(soloMapRecordEntry.getProof_img_3_link());
                existingSoloMapRecordEntry.setProof_vid_link(soloMapRecordEntry.getProof_vid_link());
                existingSoloMapRecordEntry.setStage_1_time_seconds(soloMapRecordEntry.getStage_1_time_seconds());
                existingSoloMapRecordEntry.setStage_2_time_seconds(soloMapRecordEntry.getStage_2_time_seconds());
                existingSoloMapRecordEntry.setStage_3_time_seconds(soloMapRecordEntry.getStage_3_time_seconds());
                return soloMapRecordRepository.save(existingSoloMapRecordEntry);
            case CheeselessMapRecordEntry cheeselessMapRecordEntry:
                CheeselessMapRecordEntry existingCheeselessMapRecordEntry = (CheeselessMapRecordEntry) getRecord(cheeselessMapRecordEntry.getId(), "cheeseless");
                brokenWR = existingCheeselessMapRecordEntry.getCurr_wr_seconds();
                existingCheeselessMapRecordEntry.setMap_name(cheeselessMapRecordEntry.getMap_name());
                existingCheeselessMapRecordEntry.setCurr_wr_seconds(cheeselessMapRecordEntry.getCurr_wr_seconds());
                existingCheeselessMapRecordEntry.setPrev_wr_seconds(brokenWR);
                existingCheeselessMapRecordEntry.setProof_img_1_link(cheeselessMapRecordEntry.getProof_img_1_link());
                existingCheeselessMapRecordEntry.setProof_img_2_link(cheeselessMapRecordEntry.getProof_img_2_link());
                existingCheeselessMapRecordEntry.setProof_img_3_link(cheeselessMapRecordEntry.getProof_img_3_link());
                existingCheeselessMapRecordEntry.setProof_vid_link(cheeselessMapRecordEntry.getProof_vid_link());
                existingCheeselessMapRecordEntry.setStage_1_time_seconds(cheeselessMapRecordEntry.getStage_1_time_seconds());
                existingCheeselessMapRecordEntry.setStage_2_time_seconds(cheeselessMapRecordEntry.getStage_2_time_seconds());
                existingCheeselessMapRecordEntry.setStage_3_time_seconds(cheeselessMapRecordEntry.getStage_3_time_seconds());
                return cheeselessMapRecordRepository.save(existingCheeselessMapRecordEntry);
            case RunAnyPercentMapRecordEntry runAnyPercentMapRecordEntry:
                RunAnyPercentMapRecordEntry existingRunAnyPercentMapRecordEntry = (RunAnyPercentMapRecordEntry) getRecord(runAnyPercentMapRecordEntry.getId(), "run_any");
                brokenWR = existingRunAnyPercentMapRecordEntry.getCurr_wr_seconds();
                existingRunAnyPercentMapRecordEntry.setMap_name(runAnyPercentMapRecordEntry.getMap_name());
                existingRunAnyPercentMapRecordEntry.setCurr_wr_seconds(runAnyPercentMapRecordEntry.getCurr_wr_seconds());
                existingRunAnyPercentMapRecordEntry.setPrev_wr_seconds(brokenWR);
                existingRunAnyPercentMapRecordEntry.setProof_img_1_link(runAnyPercentMapRecordEntry.getProof_img_1_link());
                existingRunAnyPercentMapRecordEntry.setProof_img_2_link(runAnyPercentMapRecordEntry.getProof_img_2_link());
                existingRunAnyPercentMapRecordEntry.setProof_img_3_link(runAnyPercentMapRecordEntry.getProof_img_3_link());
                existingRunAnyPercentMapRecordEntry.setProof_vid_link(runAnyPercentMapRecordEntry.getProof_vid_link());
                existingRunAnyPercentMapRecordEntry.setStage_1_time_seconds(runAnyPercentMapRecordEntry.getStage_1_time_seconds());
                existingRunAnyPercentMapRecordEntry.setStage_2_time_seconds(runAnyPercentMapRecordEntry.getStage_2_time_seconds());
                existingRunAnyPercentMapRecordEntry.setStage_3_time_seconds(runAnyPercentMapRecordEntry.getStage_3_time_seconds());
                return runAnyPercentMapRecordRepository.save(existingRunAnyPercentMapRecordEntry);
            case RunSoloMapRecordEntry runSoloMapRecordEntry:
                RunSoloMapRecordEntry existingRunSoloMapRecordEntry = (RunSoloMapRecordEntry) getRecord(runSoloMapRecordEntry.getId(), "run_solo");
                brokenWR = existingRunSoloMapRecordEntry.getCurr_wr_seconds();
                existingRunSoloMapRecordEntry.setMap_name(runSoloMapRecordEntry.getMap_name());
                existingRunSoloMapRecordEntry.setCurr_wr_seconds(runSoloMapRecordEntry.getCurr_wr_seconds());
                existingRunSoloMapRecordEntry.setPrev_wr_seconds(brokenWR);
                existingRunSoloMapRecordEntry.setProof_img_1_link(runSoloMapRecordEntry.getProof_img_1_link());
                existingRunSoloMapRecordEntry.setProof_img_2_link(runSoloMapRecordEntry.getProof_img_2_link());
                existingRunSoloMapRecordEntry.setProof_img_3_link(runSoloMapRecordEntry.getProof_img_3_link());
                existingRunSoloMapRecordEntry.setProof_vid_link(runSoloMapRecordEntry.getProof_vid_link());
                existingRunSoloMapRecordEntry.setStage_1_time_seconds(runSoloMapRecordEntry.getStage_1_time_seconds());
                existingRunSoloMapRecordEntry.setStage_2_time_seconds(runSoloMapRecordEntry.getStage_2_time_seconds());
                existingRunSoloMapRecordEntry.setStage_3_time_seconds(runSoloMapRecordEntry.getStage_3_time_seconds());
                return runSoloMapRecordRepository.save(existingRunSoloMapRecordEntry);
            case RunCheeselessMapRecordEntry runCheeselessMapRecordEntry:
                RunCheeselessMapRecordEntry existingRunCheeselessMapRecordEntry = (RunCheeselessMapRecordEntry) getRecord(runCheeselessMapRecordEntry.getId(), "run_cheeseless");
                brokenWR = existingRunCheeselessMapRecordEntry.getCurr_wr_seconds();
                existingRunCheeselessMapRecordEntry.setMap_name(runCheeselessMapRecordEntry.getMap_name());
                existingRunCheeselessMapRecordEntry.setCurr_wr_seconds(runCheeselessMapRecordEntry.getCurr_wr_seconds());
                existingRunCheeselessMapRecordEntry.setPrev_wr_seconds(brokenWR);
                existingRunCheeselessMapRecordEntry.setProof_img_1_link(runCheeselessMapRecordEntry.getProof_img_1_link());
                existingRunCheeselessMapRecordEntry.setProof_img_2_link(runCheeselessMapRecordEntry.getProof_img_2_link());
                existingRunCheeselessMapRecordEntry.setProof_img_3_link(runCheeselessMapRecordEntry.getProof_img_3_link());
                existingRunCheeselessMapRecordEntry.setProof_vid_link(runCheeselessMapRecordEntry.getProof_vid_link());
                existingRunCheeselessMapRecordEntry.setStage_1_time_seconds(runCheeselessMapRecordEntry.getStage_1_time_seconds());
                existingRunCheeselessMapRecordEntry.setStage_2_time_seconds(runCheeselessMapRecordEntry.getStage_2_time_seconds());
                existingRunCheeselessMapRecordEntry.setStage_3_time_seconds(runCheeselessMapRecordEntry.getStage_3_time_seconds());
                return runCheeselessMapRecordRepository.save(existingRunCheeselessMapRecordEntry);
            default:
                throw new IllegalStateException("Unexpected value: " + record);
        }
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