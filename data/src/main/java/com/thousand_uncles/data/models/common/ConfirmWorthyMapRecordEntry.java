package com.thousand_uncles.data.models.common;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "confirm_worthy")
public class ConfirmWorthyMapRecordEntry extends AutoIndexedMapRecordEntry {

    public ConfirmWorthyMapRecordEntry(){}

    @NotNull
    private Integer map_id;

    @Column(name = "additional")
    private String additional;

    @Column(name = "category")
    private String category;

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

    @Override
    public String toString() {
        return "ConfirmWorthyMapRecord";
    }
}
