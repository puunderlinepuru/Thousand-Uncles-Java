package com.thousand_uncles.data.repository;

import com.thousand_uncles.data.models.TestRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TestMapRecordRepository extends JpaRepository<TestRecord, Integer>{

}

