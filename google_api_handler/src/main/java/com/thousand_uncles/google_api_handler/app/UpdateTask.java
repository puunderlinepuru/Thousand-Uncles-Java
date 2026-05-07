package com.thousand_uncles.google_api_handler.app;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.UpdateValuesResponse;
import com.google.api.services.sheets.v4.model.ValueRange;
import com.thousand_uncles.google_api_handler.app.Utility.JSONHandler;
import com.thousand_uncles.google_api_handler.app.Utility.RecordFormatter;
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
        List<List<String>> newRecordsValues = cleanSheetData(readSheets("Any%", "A3", "G59"));

        MapRecordService mapRecordService = applicationContext.getBean(MapRecordService.class);
//        List<MapRecord> databaseRecords = mapRecordService.getAllRecords();
//        System.out.println(databaseRecords);

        for (int i = 0; i < newRecordsValues.size(); i++) {

//            Name
            String map_name = newRecordsValues.get(i).get(0);


//            Current WR
            short spreadsheetRecordTimeInSeconds = Short.MAX_VALUE;
            try {
                spreadsheetRecordTimeInSeconds = RecordFormatter.StringToNumber(newRecordsValues.get(i).get(1));
            } catch (NumberFormatException e){
                System.out.println("[ SPREADSHEET ERROR ] issue with WR for map " + map_name);
            }


//            Previous WR
            short spreadsheetPrevRecordTimeInSeconds = Short.MAX_VALUE;
            try {
                spreadsheetPrevRecordTimeInSeconds = RecordFormatter.StringToNumber(newRecordsValues.get(i).get(2));
            } catch (NumberFormatException e){
                System.out.println("[ SPREADSHEET ERROR ] issue with previous WR for map " + map_name);
            }

            String proof_pic_1_link = null;
            String proof_pic_2_link = null;
            String proof_pic_3_link = null;
            String proof_vid_link = null;
            Short stage_time_1 = null;
            Short stage_time_2 = null;
            Short stage_time_3 = null;

            if (newRecordsValues.get(i).get(3).charAt(0) != 'h')
            {
                stage_time_1 = RecordFormatter.StringToNumber(newRecordsValues.get(i).get(3));
                stage_time_2 = RecordFormatter.StringToNumber(newRecordsValues.get(i).get(4));
                stage_time_3 = RecordFormatter.StringToNumber(newRecordsValues.get(i).get(5));

                try {
                    proof_vid_link = newRecordsValues.get(i).get(6);
                } catch (Exception e) {
                    System.out.println("[ RECORD MESSAGE ] no video proof, skipping");
                }

                i++;

                proof_pic_1_link = newRecordsValues.get(i).get(3);
                proof_pic_2_link = newRecordsValues.get(i).get(4);
                proof_pic_3_link = newRecordsValues.get(i).get(5);
            } else {
                proof_pic_1_link = newRecordsValues.get(i).get(3);
                try {
                    proof_vid_link = newRecordsValues.get(i).get(4);
                } catch (Exception e) {
                    System.out.println("[ RECORD MESSAGE ] no video proof, skipping");
                }
            }

//            Database record object
            MapRecord databaseMapRecord= mapRecordService.getRecordByName(map_name);
            System.out.println(" got from database: " + databaseMapRecord);

            if (databaseMapRecord == null) {
                System.out.println("[ DATABASE MESSAGE ] record doesn't exits, adding map " + map_name + " with data " + spreadsheetRecordTimeInSeconds);
                mapRecordService.addRecord(
                        map_name,
                        spreadsheetRecordTimeInSeconds,
                        spreadsheetPrevRecordTimeInSeconds,
                        proof_pic_1_link,
                        proof_pic_2_link,
                        proof_pic_3_link,
                        proof_vid_link,
                        stage_time_1,
                        stage_time_2,
                        stage_time_3
                );
                continue;
            }

            int databaseRecordTimeInSeconds;
            try{
                databaseRecordTimeInSeconds = databaseMapRecord.getCurr_wr_seconds();
            } catch (Exception e) {
                System.out.println("[ DATABASE MESSAGE ] value is null, adding map " + map_name + " with data " + spreadsheetRecordTimeInSeconds);
                mapRecordService.addRecord(
                        map_name,
                        spreadsheetRecordTimeInSeconds,
                        spreadsheetPrevRecordTimeInSeconds,
                        proof_pic_1_link,
                        proof_pic_2_link,
                        proof_pic_3_link,
                        proof_vid_link,
                        stage_time_1,
                        stage_time_2,
                        stage_time_3
                );
                continue;
            }

            if (spreadsheetRecordTimeInSeconds <= databaseRecordTimeInSeconds) {
                continue;
            }

            boolean updated = mapRecordService.updateWR(
                    map_name,
                    spreadsheetRecordTimeInSeconds,
                    proof_pic_1_link,
                    proof_pic_2_link,
                    proof_pic_3_link,
                    proof_vid_link,
                    stage_time_1,
                    stage_time_2,
                    stage_time_3
            );
            System.out.println("[ RECORD BEATEN ] for map " + map_name + ": " + databaseRecordTimeInSeconds + " -> " + spreadsheetRecordTimeInSeconds);
            if (!updated){
                System.out.println("[ ERROR ] Error updating map record in database");
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
            System.out.println("    got data: " + dirtyMapData);
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