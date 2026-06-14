package com.thousand_uncles.discord_bot.data.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "solo")
public class SoloMapRecord extends MapRecord {

    @Column(name = "the_hero")
    private String the_hero;

    public String getThe_hero() {
        return the_hero;
    }

    public void setThe_hero(String the_hero) {
        this.the_hero = the_hero;
    }
}
