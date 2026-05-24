package com.thousand_uncles.google_api_handler.app;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.UpdateValuesResponse;
import com.google.api.services.sheets.v4.model.ValueRange;
import com.thousand_uncles.google_api_handler.app.Utility.JSONHandler;
import com.thousand_uncles.google_api_handler.data.util.RecordFormatter;
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
    List<List<Object>> valuesToUpdateOnSpreadsheet;

    ApplicationContext applicationContext;

    UpdateTask(String spreadSheetId, String valueInputOption, Sheets service, ApplicationContext applicationContext){
        this.spreadsheetId = spreadSheetId;
        this.valueInputOption = valueInputOption;
        this.service = service;
        this.applicationContext = applicationContext;
    }

    public void run() {
//        Any%
        List<List<String>> SpreadsheetRecordValues = cleanSheetData(readSheets("Any%", "A3", "G59"));

        MapRecordService mapRecordService = applicationContext.getBean(MapRecordService.class);

        System.out.println("[ STATE CHANGE ] Comparing spreadsheet records to database.. ");

        for (int i = 0; i < SpreadsheetRecordValues.size(); i++) {

            /*if (SpreadsheetRecordValues.get(i).size() < 4) {
                System.out.println("[ SPREADSHEET WARNING ] Length requirement not met. Something's missing");
                continue;
            }*/

//            Name
            String map_name = SpreadsheetRecordValues.get(i).get(0);


//            Current WR
            short spreadsheetRecordTimeInSeconds;
            try {
                spreadsheetRecordTimeInSeconds = RecordFormatter.StringToNumber(SpreadsheetRecordValues.get(i).get(1));
            } catch (NumberFormatException e){
                System.out.println("[ SPREADSHEET RECORD ERROR ] Issue with WR number format for map " + map_name + ". String in spreadsheets: " + SpreadsheetRecordValues.get(i).get(2) + ". Skipping..");
                continue;
            }


//            Previous WR
            short spreadsheetPrevRecordTimeInSeconds;
            try {
                spreadsheetPrevRecordTimeInSeconds = RecordFormatter.StringToNumber(SpreadsheetRecordValues.get(i).get(2));
            } catch (NumberFormatException e){
                System.out.println("[ SPREADSHEET RECORD ERROR ] Issue with previous WR number for map " + map_name + ". String in spreadsheets: " + SpreadsheetRecordValues.get(i).get(2) + ". Skipping..");
                continue;
            }

            String proof_pic_1_link;
            String proof_pic_2_link = null;
            String proof_pic_3_link = null;
            String proof_vid_link = null;
            Short stage_time_1 = null;
            Short stage_time_2 = null;
            Short stage_time_3 = null;

            if (SpreadsheetRecordValues.get(i).get(3).isEmpty()){
                System.out.println("[ SPREADSHEET RECORD ERROR ] Proof image field is empty for map " + map_name + ", skipping");
                continue;
            }

//            Stage times for multistage maps
            if (SpreadsheetRecordValues.get(i).get(3).charAt(0) != 'h')
            {
//                System.out.println("[ SPREADSHEET RECORD INFO ] Not a link detected, processing as stage times.. ");
                stage_time_1 = RecordFormatter.StringToNumber(SpreadsheetRecordValues.get(i).get(3));
                stage_time_2 = RecordFormatter.StringToNumber(SpreadsheetRecordValues.get(i).get(4));
                stage_time_3 = RecordFormatter.StringToNumber(SpreadsheetRecordValues.get(i).get(5));

                try {
                    proof_vid_link = SpreadsheetRecordValues.get(i).get(6);
                } catch (Exception e) {
                    System.out.println("[ SPREADSHEET RECORD INFO ] " + map_name + " no video proof");
                }

//                Moving to the next line
                i++;

                proof_pic_1_link = SpreadsheetRecordValues.get(i).get(3);
                proof_pic_2_link = SpreadsheetRecordValues.get(i).get(4);
                proof_pic_3_link = SpreadsheetRecordValues.get(i).get(5);
            } else {
                proof_pic_1_link = SpreadsheetRecordValues.get(i).get(3);
                try {
                    proof_vid_link = SpreadsheetRecordValues.get(i).get(4);
                } catch (Exception e) {
                    System.out.println("[ SPREADSHEET RECORD INFO ] " + map_name + " no video proof.");
                }
            }

//            Database record object
            MapRecord databaseMapRecord= mapRecordService.getRecordByName(map_name);

            if (databaseMapRecord == null) {
                System.out.println("[ DATABASE RECORD INFO ] Record doesn't exits in database, adding map " + map_name + " with data " + spreadsheetRecordTimeInSeconds);
                try {
                    MapRecord savedMap = mapRecordService.addRecord(
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
                } catch (Exception e) {
                    System.out.println("[ DATABASE RECORD ERROR ] Error adding new record to database: \n" + e);
                    continue;
                }
                continue;
            }

            int databaseRecordTimeInSeconds;
            try{
                databaseRecordTimeInSeconds = databaseMapRecord.getCurr_wr_seconds();
            } catch (Exception e) {
                System.out.println("[ DATABASE RECORD MESSAGE ] " + map_name + " doesn't have valid WR time. I don't know what to do, skipping... ");
                /*mapRecordService.addRecord(
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
                );*/
                continue;
            }

            if (spreadsheetRecordTimeInSeconds <= databaseRecordTimeInSeconds) {
                continue;
            }

            System.out.println("[ RECORD BEATEN ] for map " + map_name + ": " + databaseRecordTimeInSeconds + " -> " + spreadsheetRecordTimeInSeconds);

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
            if (!updated){
                System.out.println("[ DATABASE RECORD ERROR ] Error updating map record in database");
            }
        }

        System.out.println("[ STATE UPDATE ] Comparing Spreadsheet values to JSON values.. ");

        try{
            JsonNode oldRecords = JSONHandler.readRecordsJSON("records.json");
            JsonNode beatenRecords = JSONHandler.beatenRecords(oldRecords, SpreadsheetRecordValues);
            if (!beatenRecords.isEmpty()) {
                System.out.println("[ JSON INFO ] Beaten records: \n" + beatenRecords.toPrettyString());
                JSONHandler.writeRecordsJSON("records.json", SpreadsheetRecordValues);
            }
//        write or not write
//        updateSheets();
        } catch (IOException e) {
            JSONHandler.writeRecordsJSON("records.json", SpreadsheetRecordValues);
        }
    }

    protected List<List<Object>> readSheets(String sheet, String topLeft, String bottomRight) {

        System.out.println("[ STATUS UPDATE ] Reading Raw records from the Spreadsheet");
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

        System.out.println("[ STATUS UPDATE ] Cleaning records read from the Spreadsheet");
        String mapName;
        List<Object> dirtyMapData;
        List<String> cleanMapData;
        List<List<String>> cleanRecords = new ArrayList<>();

        for (int i = 0; i < unprocessedSheetData.size(); i++) {
//            Get line
            dirtyMapData = unprocessedSheetData.get(i);
            mapName = (String) dirtyMapData.getFirst();
//            System.out.println("[ INFO ] Cleaning Spreadsheet data for: " + mapName);

//            Make each field a String
            cleanMapData = dirtyMapData.stream().map(Object::toString).toList();

            if (cleanMapData.size() < 4 || cleanMapData.size() > 7) {
                System.out.println("[ ERROR ] Map:  " + mapName + " at index " + i + " has weird size. Skipping..");
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