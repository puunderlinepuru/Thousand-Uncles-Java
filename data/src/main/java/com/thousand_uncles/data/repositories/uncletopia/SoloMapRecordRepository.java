package com.thousand_uncles.data.repositories.uncletopia;

import com.thousand_uncles.data.models.common.ManualIndexedMapRecordEntry;
import com.thousand_uncles.data.models.uncletopia.SoloMapRecordEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface SoloMapRecordRepository extends JpaRepository<SoloMapRecordEntry, Integer> {

    @Query("SELECT m FROM SoloMapRecordEntry m WHERE LOWER(m.map_name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<ManualIndexedMapRecordEntry> findByMap_nameContaining(@Param("name") String name);

    @SuppressWarnings("unused")
    @Modifying
    @Query("UPDATE SoloMapRecordEntry m SET " +
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
    int updateRecordByName(
            @Param("name")          String name,
            @Param("newTime")       BigDecimal newTime,
            @Param("stage1_proof")  String stage_1_proof,
            @Param("stage2_proof")  String stage_2_proof,
            @Param("stage3_proof")  String stage_3_proof,
            @Param("proof_vid")     String proof_vid,
            @Param("stage1_time")  BigDecimal stage_1_time,
            @Param("stage2_time")  BigDecimal stage_2_time,
            @Param("stage3_time")  BigDecimal stage_3_time
    );
}