package com.thousand_uncles.google_api_handler.data.models;

import jakarta.annotation.Nullable;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;

@MappedSuperclass
public abstract class MapRecord{

//    @Version
//    private Long version;

    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull
    private Integer id;

    @Column(name = "map_name")
    private String map_name;

    @Column(name = "curr_wr_seconds", nullable = false, precision = 5)
    private Short curr_wr_seconds; // SmallInt usually maps to Short or Integer in Java

    @Column(name = "prev_wr_seconds", nullable = false, precision = 5)
    private Short prev_wr_seconds;

    @Column(name = "proof_img_1_link", nullable = false)
    private String proof_img_1_link;

    @Nullable
    @Column(name = "proof_img_2_link")
    private String proof_img_2_link;

    @Nullable
    @Column(name = "proof_img_3_link")
    private String proof_img_3_link;

    @Nullable
    @Column(name = "proof_vid_link")
    private String proof_vid_link;

    @Nullable
    @Column(name = "stage_1_time_seconds", precision = 5)
    private Short stage_1_time_seconds;

    @Nullable
    @Column(name = "stage_2_time_seconds", precision = 5)
    private Short stage_2_time_seconds;

    @Nullable
    @Column(name = "stage_3_time_seconds", precision = 5)
    private Short stage_3_time_seconds;

    // Constructors
    @SuppressWarnings("unused")
    public MapRecord() {}

    // Getters and Setters

//    ID
    @SuppressWarnings("unused")
    public Integer getId() {
        return id;
    }

    @SuppressWarnings("unused")
    public void setId(Integer id) {
        this.id = id;
    }

//    map_name
    @SuppressWarnings("unused")
    public String getMap_name() {
        return map_name;
    }

    @SuppressWarnings("unused")
    public void setMap_name(String map_name) {
        this.map_name = map_name;
    }

//    curr_wr_seconds
    @SuppressWarnings("unused")
    public short getCurr_wr_seconds(){
        return curr_wr_seconds;
    }

    @SuppressWarnings("unused")
    public void setCurr_wr_seconds(short curr_wr_seconds){
        this.curr_wr_seconds = curr_wr_seconds;
    }

//    prev_wr_seconds
    @SuppressWarnings("unused")
    public short getPrev_wr_seconds(){
        return prev_wr_seconds;
    }

    @SuppressWarnings("unused")
    public void setPrev_wr_seconds(short prev_wr_seconds){
        this.prev_wr_seconds = prev_wr_seconds;
    }

//    proof_img_1_link
    @SuppressWarnings("unused")
    public String getProof_img_1_link(){
        return proof_img_1_link;
    }

    @SuppressWarnings("unused")
    public void setProof_img_1_link(String proof_img_1_link){
        this.proof_img_1_link = proof_img_1_link;
    }

//    proof_img_2_link
    @SuppressWarnings("unused")
    @Nullable
    public String getProof_img_2_link(){
        return proof_img_2_link;
    }

    @SuppressWarnings("unused")
    public void setProof_img_2_link(@Nullable String proof_img_2_link){
        this.proof_img_2_link = proof_img_2_link;
    }

//    proof_img_3_link
    @SuppressWarnings("unused")
    @Nullable
    public String getProof_img_3_link(){
        return proof_img_3_link;
    }

    @SuppressWarnings("unused")
    public void setProof_img_3_link(@Nullable String proof_img_3_link){
        this.proof_img_3_link = proof_img_3_link;
    }

//    proof_vid_link
    @SuppressWarnings("unused")
    @Nullable
    public String getProof_vid_link(){
        return proof_vid_link;
    }

    @SuppressWarnings("unused")
    public void setProof_vid_link(@Nullable String proof_vid_link){
        this.proof_vid_link = proof_vid_link;
    }

//    stage_1_time_seconds
    @SuppressWarnings("unused")
    @Nullable
    public Short getStage_1_time_seconds(){
        return stage_1_time_seconds;
    }

    @SuppressWarnings("unused")
    public void setStage_1_time_seconds(@Nullable Short stage_1_time_seconds){
        this.stage_1_time_seconds = stage_1_time_seconds;
    }

//    stage_2_time_seconds
    @SuppressWarnings("unused")
    @Nullable
    public Short getStage_2_time_seconds(){
        return stage_2_time_seconds;
    }

    @SuppressWarnings("unused")

    public void setStage_2_time_seconds(@Nullable Short stage_2_time_seconds){
        this.stage_2_time_seconds = stage_2_time_seconds;
    }

//    stage_3_time_seconds
    @SuppressWarnings("unused")
    @Nullable
    public Short getStage_3_time_seconds(){
        return stage_3_time_seconds;
    }

    @SuppressWarnings("unused")
    public void setStage_3_time_seconds(@Nullable Short stage_3_time_seconds){
        this.stage_3_time_seconds = stage_3_time_seconds;
    }

    @Override
    public String toString() {
        return "MapRecord{" +
                "id=" + id +
                ", map_name='" + map_name + '\'' +
                '}';
    }
}
