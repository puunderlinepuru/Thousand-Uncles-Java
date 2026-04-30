package com.thousand_uncles.dashboard.data.models;

import jakarta.persistence.*;

@Entity
@Table(name = "any_percent")
public class MapRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "map_name")
    private String mapName;

    // Constructors
    @SuppressWarnings("unused")
    public MapRecord() {}

    @SuppressWarnings("unused")
    public MapRecord(String mapName) {
        this.mapName = mapName;
    }

    // Getters and Setters
    @SuppressWarnings("unused")
    public Integer getId() {
        return id;
    }

    @SuppressWarnings("unused")
    public void setId(Integer id) {
        this.id = id;
    }

    @SuppressWarnings("unused")
    public String getMapName() {
        return mapName;
    }

    @SuppressWarnings("unused")
    public void setMapName(String mapName) {
        this.mapName = mapName;
    }

    @Override
    public String toString() {
        return "MapRecord{" +
                "id=" + id +
                ", mapName='" + mapName + '\'' +
                '}';
    }
}
