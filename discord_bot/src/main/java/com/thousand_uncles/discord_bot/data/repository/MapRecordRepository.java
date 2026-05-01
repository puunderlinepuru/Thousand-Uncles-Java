package com.thousand_uncles.discord_bot.data.repository;

import com.thousand_uncles.discord_bot.data.models.MapRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MapRecordRepository extends JpaRepository<MapRecord, Integer> {

    @Query("SELECT m FROM MapRecord m WHERE m.mapName = :name")
    MapRecord findByMapName(@Param("name") String name);

    @Query("SELECT m FROM MapRecord m WHERE LOWER(m.mapName) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<MapRecord> findByMapNameContaining(@Param("name") String name);

    @Modifying
    @Query("UPDATE MapRecord m SET m.mapName = :newName WHERE m.mapName = :oldName")
    int updateMapName(@Param("oldName") String oldName, @Param("newName") String newName);

    @Modifying
    @Query("DELETE FROM MapRecord m WHERE m.mapName = :name")
    int deleteByMapName(@Param("name") String name);
}