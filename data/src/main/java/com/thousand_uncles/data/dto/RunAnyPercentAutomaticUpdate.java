package com.thousand_uncles.data.dto;

import com.thousand_uncles.data.models.run.RunAnyPercentMapRecordEntry;

import java.math.BigDecimal;

public class RunAnyPercentAutomaticUpdate extends AutomaticMapRecordUpdateSuperclass{
    public RunAnyPercentAutomaticUpdate(){}
    public RunAnyPercentAutomaticUpdate(
        Integer id,
        String mapName,
        BigDecimal newWR,
        String authenticitySignature,
        BigDecimal stage1_time,
        BigDecimal stage2_time,
        BigDecimal stage3_time
    ){

    }
}
