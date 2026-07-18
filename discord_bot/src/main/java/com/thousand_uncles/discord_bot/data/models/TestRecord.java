package com.thousand_uncles.discord_bot.data.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

@Entity
@Table(name = "test")
public class TestRecord {
    @Id
    @NotNull
    private Integer id;

    @Column(name = "map_name")
    private String map_name;

    @Column(name = "curr_wr_seconds", nullable = false)
    private BigDecimal curr_wr_seconds; // SmallInt usually maps to Short or Integer in Java

    @Column(name = "prev_wr_seconds")
    private BigDecimal prev_wr_seconds;

    @Column(name = "proof_img_1_link", nullable = false)
    private String proof_img_1_link;

    @Column(name = "proof_img_2_link")
    private String proof_img_2_link;

    @Column(name = "proof_img_3_link")
    private String proof_img_3_link;

    @Column(name = "proof_vid_link")
    private String proof_vid_link;

    @Column(name = "stage_1_time_seconds")
    private BigDecimal stage_1_time_seconds;

    @Column(name = "stage_2_time_seconds")
    private BigDecimal stage_2_time_seconds;

    @Column(name = "stage_3_time_seconds")
    private BigDecimal stage_3_time_seconds;

    public TestRecord(){}

    public TestRecord(
            int id,
            String map_name,
            BigDecimal curr_wr_seconds,
            BigDecimal prev_wr_seconds,
            String proof_img_1_link,
            String proof_img_2_link,
            String proof_img_3_link,
            String proof_vid_link,
            BigDecimal stage_1_time_seconds,
            BigDecimal stage_2_time_seconds,
            BigDecimal stage_3_time_seconds
    ){
        this.id = id;
        this.map_name = map_name;
        this.curr_wr_seconds = curr_wr_seconds;
        this.prev_wr_seconds = prev_wr_seconds;
        this.proof_img_1_link = proof_img_1_link;
        this.proof_img_2_link = proof_img_2_link;
        this.proof_img_3_link = proof_img_3_link;
        this.proof_vid_link = proof_vid_link;
        this.stage_1_time_seconds = stage_1_time_seconds;
        this.stage_2_time_seconds = stage_2_time_seconds;
        this.stage_3_time_seconds = stage_3_time_seconds;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMap_name() {
        return map_name;
    }

    public void setMap_name(String map_name) {
        this.map_name = map_name;
    }

    public BigDecimal getCurr_wr_seconds() {
        return curr_wr_seconds;
    }

    public void setCurr_wr_seconds(BigDecimal curr_wr_seconds) {
        this.curr_wr_seconds = curr_wr_seconds;
    }

    public BigDecimal getPrev_wr_seconds() {
        return prev_wr_seconds;
    }

    public void setPrev_wr_seconds(BigDecimal prev_wr_seconds) {
        this.prev_wr_seconds = prev_wr_seconds;
    }

    public String getProof_img_1_link() {
        return proof_img_1_link;
    }

    public void setProof_img_1_link(String proof_img_1_link) {
        this.proof_img_1_link = proof_img_1_link;
    }

    public String getProof_img_2_link() {
        return proof_img_2_link;
    }

    public void setProof_img_2_link(String proof_img_2_link) {
        this.proof_img_2_link = proof_img_2_link;
    }

    public String getProof_img_3_link() {
        return proof_img_3_link;
    }

    public void setProof_img_3_link(String proof_img_3_link) {
        this.proof_img_3_link = proof_img_3_link;
    }

    public String getProof_vid_link() {
        return proof_vid_link;
    }

    public void setProof_vid_link(String proof_vid_link) {
        this.proof_vid_link = proof_vid_link;
    }

    public BigDecimal getStage_1_time_seconds() {
        return stage_1_time_seconds;
    }

    public void setStage_1_time_seconds(BigDecimal stage_1_time_seconds) {
        this.stage_1_time_seconds = stage_1_time_seconds;
    }

    public BigDecimal getStage_2_time_seconds() {
        return stage_2_time_seconds;
    }

    public void setStage_2_time_seconds(BigDecimal stage_2_time_seconds) {
        this.stage_2_time_seconds = stage_2_time_seconds;
    }

    public BigDecimal getStage_3_time_seconds() {
        return stage_3_time_seconds;
    }

    public void setStage_3_time_seconds(BigDecimal stage_3_time_seconds) {
        this.stage_3_time_seconds = stage_3_time_seconds;
    }
}
