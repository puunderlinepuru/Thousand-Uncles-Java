package com.thousand_uncles.google_api_handler.data.models;

import jakarta.persistence.*;

@Entity
@Table(name = "any_percent")
public class MapRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "map_name")
    private String map_name;

    @Column(name = "curr_wr_seconds")
    private short curr_wr_seconds;

    // Constructors
    @SuppressWarnings("unused")
    public MapRecord() {}

    @SuppressWarnings("unused")
    public MapRecord(String map_name, short curr_wr_seconds) {
        this.map_name = map_name;
        this.curr_wr_seconds = curr_wr_seconds;
    }

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
    public int getCurr_wr_seconds(){
        return curr_wr_seconds;
    }

    @SuppressWarnings("unused")
    public void setCurr_wr_seconds(short curr_wr_seconds){
        this.curr_wr_seconds = curr_wr_seconds;
    }


    @Override
    public String toString() {
        return "MapRecord{" +
                "id=" + id +
                ", map_name='" + map_name + '\'' +
                '}';
    }
}
