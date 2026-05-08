package com.thousand_uncles.discord_bot.data.util;

import com.thousand_uncles.discord_bot.data.service.MapRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@SuppressWarnings("unused")
@Component
public class MapRecordUtil {

    @Autowired
    private MapRecordService service;

    public void doSomething() {
        var records = service.getAllRecords();
        System.out.println("Total records: " + records.size());
    }

    public String getMapData(String mapName){
        var record = service.getRecordByName(mapName);
        return "Database found record: " + record.toString();
    }
}
