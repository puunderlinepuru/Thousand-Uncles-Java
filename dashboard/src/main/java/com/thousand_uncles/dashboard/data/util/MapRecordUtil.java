package com.thousand_uncles.dashboard.data.util;

import com.thousand_uncles.dashboard.data.service.MapRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MapRecordUtil {

    @Autowired
    private MapRecordService service; // ✅ Spring injects this

    public void doSomething() {
        // ✅ Service is properly injected
        var records = service.getAllRecords();
        System.out.println("Total records: " + records.size());
    }

    public void processRecord(String name) {
        // ✅ Service is properly injected
        var record = service.getRecordByName(name);
        // Process record...
    }
}
