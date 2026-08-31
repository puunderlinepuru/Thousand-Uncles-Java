package com.thousand_uncles.data.models.uncletopia;

import com.thousand_uncles.data.models.common.ManualIndexedMapRecordEntry;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.math.BigDecimal;

@Entity
@Table(name = "any_percent")
public class AnyPercentMapRecordEntry extends ManualIndexedMapRecordEntry {

    public AnyPercentMapRecordEntry(){}
    public AnyPercentMapRecordEntry(
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
