package com.thousand_uncles.google_api_handler.data.repository;

import com.thousand_uncles.google_api_handler.data.models.MapRecord;
import com.thousand_uncles.google_api_handler.data.models.SoloMapRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SoloMapRecordRepository extends JpaRepository<SoloMapRecord, Integer> {

    @Query("SELECT m FROM SoloMapRecord m WHERE m.map_name = :name")
    MapRecord findByMap_name(@Param("name") String name);

    @Query("SELECT m FROM SoloMapRecord m WHERE LOWER(m.map_name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<MapRecord> findByMap_nameContaining(@Param("name") String name);

    @Modifying
    @Query("UPDATE SoloMapRecord m SET m.map_name = :newName WHERE m.map_name = :oldName")
    int updateMap_name(@Param("oldName") String oldName, @Param("newName") String newName);

    @Modifying
    @Query("DELETE FROM SoloMapRecord m WHERE m.map_name = :name")
    int deleteByMap_name(@Param("name") String name);

    @Modifying
    @Query("UPDATE SoloMapRecord m SET " +
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

//    @Modifying
//    @Query("")
//    int add
}