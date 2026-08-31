package com.thousand_uncles.data.repositories.run;

import com.thousand_uncles.data.models.common.ManualIndexedMapRecordEntry;
import com.thousand_uncles.data.models.run.RunAnyPercentMapRecordEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RunAnyPercentMapRecordRepository extends JpaRepository<RunAnyPercentMapRecordEntry, Integer> {
    @Query("SELECT m FROM RunAnyPercentMapRecordEntry m WHERE LOWER(m.map_name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<ManualIndexedMapRecordEntry> findByMap_nameContaining(@Param("name") String name);
}
