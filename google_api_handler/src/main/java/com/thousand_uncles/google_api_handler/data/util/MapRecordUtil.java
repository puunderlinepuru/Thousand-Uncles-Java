package com.thousand_uncles.google_api_handler.data.util;

import com.thousand_uncles.google_api_handler.data.service.MapRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MapRecordUtil {

    @Autowired
    private MapRecordService service;

    public void doSomething() {
        var records = service.getAllRecords();
        System.out.println("Total records: " + records.size());

//        var updated = service.updateWR("pl_upward", 50);
//        System.out.println("updated: " + updated);
    }

    public String getMapData(String mapName){
        var record = service.getRecordByName(mapName);
        return "Database found record: " + record.toString();
    }
}
