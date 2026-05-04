package com.thousand_uncles.google_api_handler.app;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.UpdateValuesResponse;
import com.google.api.services.sheets.v4.model.ValueRange;
import com.thousand_uncles.google_api_handler.data.models.MapRecord;
import com.thousand_uncles.google_api_handler.data.service.MapRecordService;
import org.springframework.context.ApplicationContext;

import java.io.IOException;
import java.util.*;

class UpdateTask extends TimerTask {
    UpdateValuesResponse result;
    String valueInputOption;
    Sheets service;
    String spreadsheetId;
    List<List<Object>> valuesToPost;


    ApplicationContext applicationContext;

    UpdateTask(String spreadSheetId, UpdateValuesResponse result, String valueInputOption, List<List<Object>> valuesToPost, Sheets service, ApplicationContext applicationContext){
        this.spreadsheetId = spreadSheetId;
        this.result = result;
        this.valueInputOption = valueInputOption;
        this.valuesToPost = valuesToPost;
        this.service = service;
        this.applicationContext = applicationContext;
    }

    public void run() {
//        Any%
        List<List<String>> newRecordsValues = cleanSheetData(readSheets("Any%", "A2", "G53"));

        MapRecordService mapRecordService = applicationContext.getBean(MapRecordService.class);
//        List<MapRecord> databaseRecords = mapRecordService.getAllRecords();
//        System.out.println(databaseRecords);

        for (int i = 0; i < newRecordsValues.size(); i++) {

//            Name
            String mapName = newRecordsValues.get(i).get(0);

            int spreadsheetRecordTimeInSeconds;
            String[] timeStringParts = newRecordsValues.get(i).get(1).split(":");
            int minutes = Integer.parseInt(timeStringParts[0]);
            int seconds = Integer.parseInt(timeStringParts[1]);
            spreadsheetRecordTimeInSeconds = seconds + minutes*60;

//            Database record object
            MapRecord databaseMapRecord= mapRecordService.getRecordByName(mapName);
            System.out.println(" got from database: " + databaseMapRecord);

            if (databaseMapRecord == null) {
                System.out.println("[ DATABASE MESSAGE ] record doesn't exits, adding map " + mapName + " with data " + (short) spreadsheetRecordTimeInSeconds);
                mapRecordService.addRecord(mapName, (short) spreadsheetRecordTimeInSeconds);
                continue;
            }

            int databaseRecordTimeInSeconds;
            try{
                databaseRecordTimeInSeconds = databaseMapRecord.getCurr_wr_seconds();
            } catch (Exception e) {
                System.out.println("[ DATABASE MESSAGE ] value is null, adding map " + mapName + " with data " + (short) spreadsheetRecordTimeInSeconds);
                mapRecordService.addRecord(mapName, (short) spreadsheetRecordTimeInSeconds);
                continue;
            }


            if (spreadsheetRecordTimeInSeconds > databaseRecordTimeInSeconds){
                boolean updated = mapRecordService.updateWR(mapName, spreadsheetRecordTimeInSeconds);
                System.out.println("[ RECORD BEATEN ] for map " + mapName + ": " + databaseRecordTimeInSeconds + " -> " + spreadsheetRecordTimeInSeconds);
                if (!updated){
                    System.out.println("[ ERROR ] Error updating map record in database");
                }
            }
        }

//        if (newRecordsValues.size() < databaseRecords.size()){
//            System.out.println("[ DATABASE MESSAGE ] More records in DB than were loaded from spreadsheet, I suggest you check that");
//        }

        try{
            JsonNode oldRecords = JSONHandler.readRecordsJSON("records.json");
            JsonNode beatenRecords = JSONHandler.beatenRecords(oldRecords, newRecordsValues);
            if (!beatenRecords.isEmpty()) {
                JSONHandler.writeRecordsJSON("records.json", newRecordsValues);
            }
//        write or not write
//        updateSheets();
        } catch (IOException e) {
            JSONHandler.writeRecordsJSON("records.json", newRecordsValues);
        }


    }

    protected List<List<Object>> readSheets(String sheet, String topLeft, String bottomRight) {
        try {
            ValueRange response = service.spreadsheets().values()
                    .get(spreadsheetId, sheet + "!" + topLeft + ":" + bottomRight)
//                    .get(spreadsheetId, "Sheet1!D2")
                    .execute();

            if (!response.isEmpty()) {
                return response.getValues();
            } else {
                System.out.println("No data found.");
                return null;
            }
        } catch (Exception ex) {
            try {
                throw ex;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    protected List<List<String>> cleanSheetData(List<List<Object>> unprocessedSheetData){
        String mapName;
        List<Object> dirtyMapData;
        List<String> cleanMapData;
        List<List<String>> cleanRecords = new ArrayList<>();

        for (int i = 0; i < unprocessedSheetData.size(); i++) {
            dirtyMapData = unprocessedSheetData.get(i);
            mapName = (String) dirtyMapData.getFirst();
            cleanMapData = new ArrayList<>();

            System.out.println("analyzing " + mapName);

//            Make fields Strings
            cleanMapData = dirtyMapData.stream().map(Object::toString).toList();

            if (cleanMapData.size() < 4 || cleanMapData.size() > 7) {
                System.out.println("[ ERROR ] element " + mapName + " at " + i + " has weird size");
                continue;
            }
            cleanRecords.add(cleanMapData);
        }

        return cleanRecords;
    }


    /*protected void updateSheets() {
        try {
            // Updates the values in the specified range.
            ValueRange body = new ValueRange()
                    .setValues(values);
            result = service.spreadsheets().values().update(spreadsheetId, "Sheet1!A2", body)
                    .setValueInputOption(valueInputOption)
                    .execute();
            System.out.printf("%d cells updated.\n", result.getUpdatedCells());
        } catch (GoogleJsonResponseException e) {
            // TODO(developer) - handle error appropriately
            GoogleJsonError error = e.getDetails();
            if (error.getCode() == 404) {
                System.out.printf("Spreadsheet not found with id '%s'.\n", spreadsheetId);
            } else {
                try {
                    throw e;
                } catch (GoogleJsonResponseException ex) {
                    throw new RuntimeException(ex);
                }
            }
        } catch (Exception ex) {
            try {
                throw ex;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }*/
}