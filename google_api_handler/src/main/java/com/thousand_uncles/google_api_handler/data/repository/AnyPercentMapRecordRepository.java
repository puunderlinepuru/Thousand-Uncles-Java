package com.thousand_uncles.google_api_handler.data.repository;

import com.thousand_uncles.google_api_handler.data.models.AnyPercentMapRecord;
import com.thousand_uncles.google_api_handler.data.models.MapRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnyPercentMapRecordRepository extends JpaRepository<AnyPercentMapRecord, Integer> {

    @Query("SELECT m FROM AnyPercentMapRecord m WHERE m.map_name = :name")
    MapRecord findByMap_name(@Param("name") String name);

    @Query("SELECT m FROM AnyPercentMapRecord m WHERE LOWER(m.map_name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<MapRecord> findByMap_nameContaining(@Param("name") String name);

    @Modifying
    @Query("UPDATE AnyPercentMapRecord m SET m.map_name = :newName WHERE m.map_name = :oldName")
    int updateMap_name(@Param("oldName") String oldName, @Param("newName") String newName);

    @Modifying
    @Query("DELETE FROM AnyPercentMapRecord m WHERE m.map_name = :name")
    int deleteByMap_name(@Param("name") String name);

    @Modifying
    @Query("UPDATE AnyPercentMapRecord m SET " +
            "m.prev_wr_seconds = m.curr_wr_seconds, " +
            "m.curr_wr_seconds = :newTime, " +
            "m.proof_img_1_link = :stage1_proof, " +
            "m.proof_img_2_link = :stage2_proof, " +
            "m.proof_img_3_link = :stage3_proof, " +
            "m.proof_vid_link = :proof_vid, " +
            "m.stage_1_time_seconds = :stage1_time, " +
            "m.stage_2_time_seconds = :stage2_time, " +
            "m.stage_3_time_seconds = :stage3_time " +
            "WHERE m.map_name = :name")
    int updateRecord(
            @Param("name")          String name,
            @Param("newTime")       Short newTime,
            @Param("stage1_proof")  String stage_1_proof,
            @Param("stage2_proof")  String stage_2_proof,
            @Param("stage3_proof")  String stage_3_proof,
            @Param("proof_vid")     String proof_vid,
            @Param("stage1_time")  Short stage_1_time,
            @Param("stage2_time")  Short stage_2_time,
            @Param("stage3_time")  Short stage_3_time
    );

    @Modifying
    @Query(value = "INSERT INTO any_percent (id, map_name, curr_wr_seconds, prev_wr_seconds, proof_img_1_link, proof_img_2_link, proof_img_3_link, proof_vid_link, stage_1_time_seconds, stage_2_time_seconds, stage_3_time_seconds) " +
            "VALUES " +
            "(:id, :name, :curr_time, :prev_time, :stage1_proof, :stage2_proof, :stage3_proof, :proof_vid, :stage1_time, :stage2_time, :stage3_time) " +
            "ON CONFLICT (id) DO UPDATE SET " +
            "id = EXCLUDED.id, " +
            "map_name = EXCLUDED.map_name, " +
            "curr_wr_seconds = :curr_time, " +
            "prev_wr_seconds = EXCLUDED.curr_wr_seconds, " +
            "proof_img_1_link = :stage1_proof, " +
            "proof_img_2_link = :stage2_proof, " +
            "proof_img_3_link = :stage3_proof, " +
            "proof_vid_link = :proof_vid, " +
            "stage_1_time_seconds = :stage1_time, " +
            "stage_2_time_seconds = :stage2_time, " +
            "stage_3_time_seconds = :stage3_time ",
            nativeQuery = true)
    void upsert(
            @Param("id")            Integer ID,
            @Param("name")          String name,
            @Param("curr_time")       Short newTime,
            @Param("prev_time")     Short prevTime,
            @Param("stage1_proof")  String stage_1_proof,
            @Param("stage2_proof")  String stage_2_proof,
            @Param("stage3_proof")  String stage_3_proof,
            @Param("proof_vid")     String proof_vid,
            @Param("stage1_time")  Short stage_1_time,
            @Param("stage2_time")  Short stage_2_time,
            @Param("stage3_time")  Short stage_3_time
    );
}