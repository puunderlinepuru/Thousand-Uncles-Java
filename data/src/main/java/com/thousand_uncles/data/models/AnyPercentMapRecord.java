package com.thousand_uncles.data.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "any_percent")
public class AnyPercentMapRecord extends MapRecord {

    public AnyPercentMapRecord(){}
    public AnyPercentMapRecord(
            int id,
            String map_name,
            short curr_wr_seconds,
            short prev_wr_seconds,
            String proof_img_1_link,
            String proof_img_2_link,
            String proof_img_3_link,
            String proof_vid_link,
            Short stage_1_time_seconds,
            Short stage_2_time_seconds,
            Short stage_3_time_seconds
    ) {
        super(
                id,
                map_name,
                curr_wr_seconds,
                prev_wr_seconds,
                proof_img_1_link,
                proof_img_2_link,
                proof_img_3_link,
                proof_vid_link,
                stage_1_time_seconds,
                stage_2_time_seconds,
                stage_3_time_seconds
        );
    }
}
