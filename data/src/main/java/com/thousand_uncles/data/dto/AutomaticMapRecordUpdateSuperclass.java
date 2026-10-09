package com.thousand_uncles.data.dto;

import java.math.BigDecimal;

public abstract class AutomaticMapRecordUpdateSuperclass {
    private Integer id;
    private String mapName;
    private BigDecimal newWR;

    private String authenticitySignature;

    private BigDecimal stage1_time;
    private BigDecimal stage2_time;
    private BigDecimal stage3_time;

    public AutomaticMapRecordUpdateSuperclass(){}
    public AutomaticMapRecordUpdateSuperclass(
            Integer id,
            String mapName,
            BigDecimal newWR,
            String authenticitySignature,
            BigDecimal stage1_time,
            BigDecimal stage2_time,
            BigDecimal stage3_time
    ){
        this.id = id;
        this.mapName = mapName;
        this.newWR = newWR;
        this.authenticitySignature = authenticitySignature;
        this.stage1_time = stage1_time;
        this.stage2_time = stage2_time;
        this.stage3_time = stage3_time;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMapName() {
        return mapName;
    }

    public void setMapName(String mapName) {
        this.mapName = mapName;
    }

    public BigDecimal getNewWR() {
        return newWR;
    }

    public void setNewWR(BigDecimal newWR) {
        this.newWR = newWR;
    }

    public String getAuthenticitySignature() {
        return authenticitySignature;
    }

    public void setAuthenticitySignature(String authenticitySignature) {
        this.authenticitySignature = authenticitySignature;
    }

    public BigDecimal getStage1_time() {
        return stage1_time;
    }

    public void setStage1_time(BigDecimal stage1_time) {
        this.stage1_time = stage1_time;
    }

    public BigDecimal getStage2_time() {
        return stage2_time;
    }

    public void setStage2_time(BigDecimal stage2_time) {
        this.stage2_time = stage2_time;
    }

    public BigDecimal getStage3_time() {
        return stage3_time;
    }

    public void setStage3_time(BigDecimal stage3_time) {
        this.stage3_time = stage3_time;
    }
}
