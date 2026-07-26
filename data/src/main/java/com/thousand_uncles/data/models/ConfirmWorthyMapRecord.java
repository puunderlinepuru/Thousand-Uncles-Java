package com.thousand_uncles.data.models;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "confirm_worthy")
public class ConfirmWorthyMapRecord {

    public ConfirmWorthyMapRecord(){}

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    private Integer map_id;

    @Column(name = "additional")
    private String additional;

    @Column(name = "category")
    private String category;

    @Column(name = "map_name")
    private String map_name;

    @Column(name = "curr_wr_seconds", nullable = false, precision = 5)
    private Short curr_wr_seconds; // SmallInt usually maps to Short or Integer in Java

    @Column(name = "prev_wr_seconds", nullable = false, precision = 5)
    private Short prev_wr_seconds;

    @Column(name = "proof_img_1_link", nullable = false)
    private String proof_img_1_link;

    @Column(name = "proof_img_2_link")
    private String proof_img_2_link;

    @Column(name = "proof_img_3_link")
    private String proof_img_3_link;

    @Column(name = "proof_vid_link")
    private String proof_vid_link;

    @Column(name = "stage_1_time_seconds", precision = 5)
    private Short stage_1_time_seconds;

    @Column(name = "stage_2_time_seconds", precision = 5)
    private Short stage_2_time_seconds;

    @Column(name = "stage_3_time_seconds", precision = 5)
    private Short stage_3_time_seconds;

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
    public void setProof_img_2_link(String proof_img_2_link){
        this.proof_img_2_link = proof_img_2_link;
    }

    //    proof_img_3_link
    @SuppressWarnings("unused")
    @Nullable
    public String getProof_img_3_link(){
        return proof_img_3_link;
    }

    @SuppressWarnings("unused")
    public void setProof_img_3_link(String proof_img_3_link){
        this.proof_img_3_link = proof_img_3_link;
    }

    //    proof_vid_link
    @SuppressWarnings("unused")
    @Nullable
    public String getProof_vid_link(){
        return proof_vid_link;
    }

    @SuppressWarnings("unused")
    public void setProof_vid_link(String proof_vid_link){
        this.proof_vid_link = proof_vid_link;
    }

    //    stage_1_time_seconds
    @SuppressWarnings("unused")
    @Nullable
    public Short getStage_1_time_seconds(){
        return stage_1_time_seconds;
    }

    @SuppressWarnings("unused")
    public void setStage_1_time_seconds(Short stage_1_time_seconds){
        this.stage_1_time_seconds = stage_1_time_seconds;
    }

    //    stage_2_time_seconds
    @SuppressWarnings("unused")
    @Nullable
    public Short getStage_2_time_seconds(){
        return stage_2_time_seconds;
    }

    @SuppressWarnings("unused")
    public void setStage_2_time_seconds(Short stage_2_time_seconds){
        this.stage_2_time_seconds = stage_2_time_seconds;
    }

    //    stage_3_time_seconds
    @SuppressWarnings("unused")
    @Nullable
    public Short getStage_3_time_seconds(){
        return stage_3_time_seconds;
    }

    @SuppressWarnings("unused")
    public void setStage_3_time_seconds(Short stage_3_time_seconds){
        this.stage_3_time_seconds = stage_3_time_seconds;
    }

    @Override
    public String toString() {
        return "MapRecord{" +
                "id=" + id +
                ", map_name='" + map_name + '\'' +
                '}';
    }

    public String getAdditional() {
        return additional;
    }

    public void setAdditional(String additional) {
        this.additional = additional;
    }

    @SuppressWarnings("unused")
    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @SuppressWarnings("unused")
    public Integer getMap_id() {
        return map_id;
    }

    public void setMap_id(Integer map_id) {
        this.map_id = map_id;
    }
}
