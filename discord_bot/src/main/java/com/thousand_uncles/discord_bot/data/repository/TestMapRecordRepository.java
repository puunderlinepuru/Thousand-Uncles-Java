package com.thousand_uncles.discord_bot.data.repository;

import com.thousand_uncles.discord_bot.data.models.TestRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TestMapRecordRepository extends JpaRepository<TestRecord, Integer>{

}

