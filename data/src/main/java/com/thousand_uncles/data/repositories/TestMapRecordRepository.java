package com.thousand_uncles.data.repositories;

import com.thousand_uncles.data.models.run.TestRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TestMapRecordRepository extends JpaRepository<TestRecord, Integer>{

}

