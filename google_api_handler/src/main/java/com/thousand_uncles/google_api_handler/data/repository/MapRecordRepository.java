package com.thousand_uncles.google_api_handler.data.repository;

import com.thousand_uncles.google_api_handler.data.models.MapRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MapRecordRepository extends JpaRepository<MapRecord, Integer> {

    @Query("SELECT m FROM MapRecord m WHERE m.map_name = :name")
    MapRecord findByMap_name(@Param("name") String name);

    @Query("SELECT m FROM MapRecord m WHERE LOWER(m.map_name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<MapRecord> findByMap_nameContaining(@Param("name") String name);

    @Modifying
    @Query("UPDATE MapRecord m SET m.map_name = :newName WHERE m.map_name = :oldName")
    int updateMap_name(@Param("oldName") String oldName, @Param("newName") String newName);

    @Modifying
    @Query("DELETE FROM MapRecord m WHERE m.map_name = :name")
    int deleteByMap_name(@Param("name") String name);

    @Modifying
    @Query("UPDATE MapRecord m SET m.curr_wr_seconds = :newTime WHERE m.map_name = :name")
    int updateCurr_wr_seconds(@Param("name") String name, @Param("newTime") int newTime);
}