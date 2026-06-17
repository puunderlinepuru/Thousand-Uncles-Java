package com.thousand_uncles.discord_bot.data.repository;


import com.thousand_uncles.discord_bot.data.models.ConfirmWorthyMapRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ConfirmWorthyRepository extends JpaRepository<ConfirmWorthyMapRecord, Integer> {

    @Query("SELECT m FROM ConfirmWorthyMapRecord m WHERE m.map_id = :map_id AND m.category = :category")
    ConfirmWorthyMapRecord findMapByCategory(@Param("map_id") Integer map_id, @Param("category") String category);

    @SuppressWarnings("unused")
    @Modifying
    @Query("DELETE FROM ConfirmWorthyMapRecord m WHERE m.map_id = :map_id AND m.category = :category")
    int removeMapByCategory(@Param("map_id") Integer map_id, @Param("category") String category);

    @SuppressWarnings("unused")
    @Modifying
    @Query("INSERT INTO ConfirmWorthyMapRecord (category, map_id, map_name, curr_wr_seconds, prev_wr_seconds, proof_img_1_link, proof_img_2_link, proof_img_3_link, proof_vid_link, stage_1_time_seconds, stage_2_time_seconds, stage_3_time_seconds, additional) " +
            "VALUES " +
            "(:category, :id, :name, :curr_time, :prev_time, :stage1_proof, :stage2_proof, :stage3_proof, :proof_vid, :stage1_time, :stage2_time, :stage3_time, :additional)")
    void addRecord(
            @Param("category")      String category,
            @Param("id")            Integer ID,
            @Param("name")          String name,
            @Param("curr_time")     Short newTime,
            @Param("prev_time")     Short prevTime,
            @Param("stage1_proof")  String stage_1_proof,
            @Param("stage2_proof")  String stage_2_proof,
            @Param("stage3_proof")  String stage_3_proof,
            @Param("proof_vid")     String proof_vid,
            @Param("stage1_time")   Short stage_1_time,
            @Param("stage2_time")   Short stage_2_time,
            @Param("stage3_time")   Short stage_3_time,
            @Param("additional")    String additional
    );

}
