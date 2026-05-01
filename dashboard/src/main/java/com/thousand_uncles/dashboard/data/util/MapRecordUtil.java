package com.thousand_uncles.dashboard.data.util;

import com.thousand_uncles.dashboard.data.service.MapRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MapRecordUtil {

    @Autowired
    private MapRecordService service; // ✅ Spring injects this

    public void doSomething() {
        var records = service.getAllRecords();
//        var newRecord = service.addRecord("pl_goldrush");
        System.out.println("Total records: " + records.size());
    }
}
