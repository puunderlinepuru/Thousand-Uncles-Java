package com.thousand_uncles.dashboard;

import org.springframework.stereotype.Component;

@Component
public class PSQL_basics {
    PSQL_basics(){
        Thing();
    }

    public void Thing() {
        MapRecordDAO mapRecordDAO = new MapRecordDAO();
//
        MapRecord newMapRecord = new MapRecord();
        newMapRecord.setMapName("pl_goldrush");
//        mapRecordDAO.saveMap(newMapRecord);
        mapRecordDAO.removeMapByName("pl_goldrush");

        int mapId = 0;
        MapRecord mapRecord = mapRecordDAO.getMapRecordById(mapId);
        if (mapRecord != null) {
            System.out.println("Found map: " + mapRecord.getMapName());
        } else {
            System.out.println("Map not found.");
        }
    }

}

