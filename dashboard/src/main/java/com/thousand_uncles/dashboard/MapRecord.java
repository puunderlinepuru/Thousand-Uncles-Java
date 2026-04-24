package com.thousand_uncles.dashboard;

import jakarta.persistence.*;

@Entity
@Table(name = "any_percent")
//@SecondaryTables()
public class MapRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(name = "map_name")
    private String mapName;

    public String getMapName() {return mapName;}

    public void setMapName(String mapName) {this.mapName = mapName;}
}