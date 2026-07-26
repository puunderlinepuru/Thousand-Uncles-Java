package com.thousand_uncles.google_api_handler;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.ValueRange;
import com.thousand_uncles.data.models.AnyPercentMapRecord;
import com.thousand_uncles.data.models.MapRecord;
import com.thousand_uncles.data.models.SoloMapRecord;
import com.thousand_uncles.data.service.MapRecordServiceProd;
import com.thousand_uncles.data.util.RecordFormatter;
import com.thousand_uncles.google_api_handler.util.GlobalThings;
import com.thousand_uncles.google_api_handler.util.JSONHandler;
import org.springframework.context.ApplicationContext;

import java.io.IOException;
import java.util.*;

class UpdateTask extends TimerTask {
    String valueInputOption;
    Sheets service;
    String spreadsheetId;

    ApplicationContext applicationContext;

    MapRecordServiceProd mapRecordServiceProd;

    UpdateTask(String spreadSheetId, String valueInputOption, Sheets service, ApplicationContext applicationContext, MapRecordServiceProd mapRecordServiceProd){
        this.spreadsheetId = spreadSheetId;
        this.valueInputOption = valueInputOption;
        this.service = service;
        this.applicationContext = applicationContext;
        this.mapRecordServiceProd = mapRecordServiceProd;
    }

    public void run() {
        String category;
        List<List<String>> spreadsheetRecordValues;

//        Any%
        spreadsheetRecordValues = cleanSheetData(readSheets("Any%", "A3", "G59"));
        category = "any";
        System.out.println("[ STATE CHANGE ] Comparing spreadsheet records to database.. ");
        processAnyPercentSheetData(spreadsheetRecordValues, category);

//        RUN Maps%
        spreadsheetRecordValues = cleanSheetData(readSheets("RUN Maps", "A3", "G60"));
        category = "any";
        System.out.println("[ STATE CHANGE ] Comparing spreadsheet records to database.. ");
        processAnyPercentSheetData(spreadsheetRecordValues, category);

//        Solo%
        spreadsheetRecordValues = cleanSheetData(readSheets("Solo%", "A3", "H114"));
        category = "solo";
        System.out.println("[ STATE CHANGE ] Comparing spreadsheet records to database.. ");
        processSoloSheetData(spreadsheetRecordValues, category);

//        UpdateValues.updateSheets(List.of(List.of("hewwo")), "Sheet1", "A3");
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
            try{
                mapName = (String) dirtyMapData.getFirst();
            } catch (NoSuchElementException e){
                System.out.println("empty line, skipping");
                continue;
            }

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

    private void processAnyPercentSheetData(List<List<String>> spreadsheetRecordValues, String category){
        for (int i = 0; i < spreadsheetRecordValues.size(); i++) {

//            Name
            String map_name = spreadsheetRecordValues.get(i).getFirst();

//            mapName = checkName()

//            getId(mapName)

            int map_id = GlobalThings.getMapIDS().indexOf(map_name);
            if (map_id == -1){
                System.out.println(" couldn't find index for map, skipping");
                continue;
            }


//            Current WR
            short spreadsheetRecordTimeInSeconds;
            try {
                spreadsheetRecordTimeInSeconds = RecordFormatter.StringToNumber(spreadsheetRecordValues.get(i).get(1));
            } catch (NumberFormatException e){
                System.out.println("[ SPREADSHEET RECORD ERROR ] Issue with WR number format for map " + map_name + ". String in spreadsheets: " + spreadsheetRecordValues.get(i).get(2) + ". Skipping..");
                continue;
            }


//            Previous WR
            short spreadsheetPrevRecordTimeInSeconds = 0;
            try {
                spreadsheetPrevRecordTimeInSeconds = RecordFormatter.StringToNumber(spreadsheetRecordValues.get(i).get(2));
            } catch (NumberFormatException e){
                System.out.println("[ SPREADSHEET RECORD ERROR ] Issue with previous WR number for map " + map_name + ". String in spreadsheets: " + spreadsheetRecordValues.get(i).get(2) + ". Skipping..");
            }

            String proof_pic_1_link;
            String proof_pic_2_link = null;
            String proof_pic_3_link = null;
            String proof_vid_link = null;
            Short stage_time_1 = null;
            Short stage_time_2 = null;
            Short stage_time_3 = null;

            if (spreadsheetRecordValues.get(i).get(3).isEmpty()){
                System.out.println("[ SPREADSHEET RECORD ERROR ] Proof image field is empty for map " + map_name + ", skipping");
                continue;
            }

//            Stage times for multistage maps
            if (spreadsheetRecordValues.get(i).get(3).charAt(0) != 'h')
            {
//                System.out.println("[ SPREADSHEET RECORD INFO ] Not a link detected, processing as stage times. ");
                stage_time_1 = RecordFormatter.StringToNumber(spreadsheetRecordValues.get(i).get(3));
                stage_time_2 = RecordFormatter.StringToNumber(spreadsheetRecordValues.get(i).get(4));
                stage_time_3 = RecordFormatter.StringToNumber(spreadsheetRecordValues.get(i).get(5));

                try {
                    proof_vid_link = spreadsheetRecordValues.get(i).get(6);
                } catch (Exception e) {
                    System.out.println("[ SPREADSHEET RECORD INFO ] " + map_name + " no video proof");
                }

//                Moving to the next line
                i++;

                proof_pic_1_link = spreadsheetRecordValues.get(i).get(3);
                proof_pic_2_link = spreadsheetRecordValues.get(i).get(4);
                proof_pic_3_link = spreadsheetRecordValues.get(i).get(5);
            } else {
                proof_pic_1_link = spreadsheetRecordValues.get(i).get(3);
                try {
                    proof_vid_link = spreadsheetRecordValues.get(i).get(4);
                } catch (Exception e) {
                    System.out.println("[ SPREADSHEET RECORD INFO ] " + map_name + " no video proof.");
                }
            }

            MapRecord databaseMapRecord;
//            Database record object
            try {
                databaseMapRecord = mapRecordServiceProd.getRecord(map_id, category);
            } catch (NoSuchElementException e){
                System.out.println("[ DATABASE RECORD ERROR ] Couldn't find " + map_name + " in the data, skipping comparison");
                try {
                    AnyPercentMapRecord savedRecord = (AnyPercentMapRecord) mapRecordServiceProd.saveAny(
                            map_id,
                            map_name,
                            spreadsheetRecordTimeInSeconds,
                            spreadsheetPrevRecordTimeInSeconds,
                            proof_pic_1_link,
                            proof_pic_2_link,
                            proof_pic_3_link,
                            proof_vid_link,
                            stage_time_1,
                            stage_time_2,
                            stage_time_3);
                } catch (Exception exception) {
                    System.out.println("[ DATABASE RECORD ERROR ] Error adding new record to database: \n" + exception);
                }
                continue;
            }

            int databaseRecordTimeInSeconds;
            try{
                databaseRecordTimeInSeconds = databaseMapRecord.getCurr_wr_seconds();
            } catch (Exception e) {
                System.out.println("[ DATABASE RECORD MESSAGE ] " + map_name + " doesn't have valid WR time. I don't know what to do, skipping... ");
                continue;
            }

            if (spreadsheetRecordTimeInSeconds >= databaseRecordTimeInSeconds) {
                continue;
            }

            System.out.println("[ RECORD BEATEN ] for map " + map_name + ": " + databaseRecordTimeInSeconds + " -> " + spreadsheetRecordTimeInSeconds);
            AnyPercentMapRecord updatedRecord = (AnyPercentMapRecord) mapRecordServiceProd.updateAny(
                    map_id,
                    map_name,
                    spreadsheetRecordTimeInSeconds,
                    spreadsheetPrevRecordTimeInSeconds,
                    proof_pic_1_link,
                    proof_pic_2_link,
                    proof_pic_3_link,
                    proof_vid_link,
                    stage_time_1,
                    stage_time_2,
                    stage_time_3);
        }

        System.out.println("[ STATE UPDATE ] Comparing Spreadsheet values to JSON values.. ");

        try{
            JsonNode oldRecords = JSONHandler.readRecordsJSON("records.json");
            JsonNode beatenRecords = JSONHandler.beatenRecords(oldRecords, spreadsheetRecordValues);
            if (!beatenRecords.isEmpty()) {
                System.out.println("[ JSON INFO ] Beaten records: \n" + beatenRecords.toPrettyString());
                JSONHandler.writeRecordsJSON("records.json", spreadsheetRecordValues);
            }
//        write or not write
//        updateSheets();
        } catch (IOException e) {
            JSONHandler.writeRecordsJSON("records.json", spreadsheetRecordValues);
        }
    }

    private void processSoloSheetData(List<List<String>> spreadsheetRecordValues, String category){
        for (int i = 0; i < spreadsheetRecordValues.size(); i++) {

            System.out.println("given line array: " + spreadsheetRecordValues.get(i));

//            Name
            String map_name = spreadsheetRecordValues.get(i).getFirst().split(" ")[0];


            int map_id = GlobalThings.getMapIDS().indexOf(map_name);
            if (map_id == -1){
                System.out.println(" couldn't find index for map, skipping");
                continue;
            }


//            Current WR
            short spreadsheetRecordTimeInSeconds;
            try {
                spreadsheetRecordTimeInSeconds = RecordFormatter.StringToNumber(spreadsheetRecordValues.get(i).get(1));
            } catch (NumberFormatException e){
                System.out.println("[ SPREADSHEET RECORD ERROR ] Issue with WR number format for map " + map_name + ". String in spreadsheets: " + spreadsheetRecordValues.get(i).get(2) + ". Skipping..");
                continue;
            }


//            Previous WR
            short spreadsheetPrevRecordTimeInSeconds = 0;
            try {
                spreadsheetPrevRecordTimeInSeconds = RecordFormatter.StringToNumber(spreadsheetRecordValues.get(i).get(2));
            } catch (NumberFormatException e){
                System.out.println("[ SPREADSHEET RECORD ERROR ] Issue with previous WR number for map " + map_name + ". String in spreadsheets: " + spreadsheetRecordValues.get(i).get(2) + ".");
            }

            String proof_pic_1_link;
            String proof_pic_2_link = null;
            String proof_pic_3_link = null;
            String proof_vid_link = null;
            Short stage_time_1 = null;
            Short stage_time_2 = null;
            Short stage_time_3 = null;

//            THE HERO
            String theHero;
            try{
                theHero = spreadsheetRecordValues.get(i).get(3);
            } catch (Exception e) {
                System.out.println("no hero? skipping");
                continue;
            }

            // Proof pics
            try{
                proof_pic_1_link = spreadsheetRecordValues.get(i).get(4);
            } catch (Exception e) {
                System.out.println(" couldn't get image link, it might be blank, skipping ");
                if (spreadsheetRecordValues.get(i+1).getFirst().isEmpty()){
                    i++;
                }
                continue;
            }
            if (spreadsheetRecordValues.get(i).get(4).isEmpty()){
                System.out.println("[ SPREADSHEET RECORD ERROR ] Proof image field is empty for map " + map_name + ", skipping");
                continue;
            }

//            Stage times for multistage maps
            if (spreadsheetRecordValues.get(i).get(4).charAt(0) != 'h')
            {
//                System.out.println("[ SPREADSHEET RECORD INFO ] Not a link detected, processing as stage times. ");
                stage_time_1 = RecordFormatter.StringToNumber(spreadsheetRecordValues.get(i).get(4));
                stage_time_2 = RecordFormatter.StringToNumber(spreadsheetRecordValues.get(i).get(5));
                stage_time_3 = RecordFormatter.StringToNumber(spreadsheetRecordValues.get(i).get(6));

                try {
                    proof_vid_link = spreadsheetRecordValues.get(i).get(7);
                } catch (Exception e) {
                    System.out.println("[ SPREADSHEET RECORD INFO ] " + map_name + " no video proof");
                }

//                Moving to the next line
                i++;

                proof_pic_1_link = spreadsheetRecordValues.get(i).get(4);
                proof_pic_2_link = spreadsheetRecordValues.get(i).get(5);
                proof_pic_3_link = spreadsheetRecordValues.get(i).get(6);
            } else {
                proof_pic_1_link = spreadsheetRecordValues.get(i).get(4);
                try {
                    proof_vid_link = spreadsheetRecordValues.get(i).get(5);
                } catch (Exception e) {
                    System.out.println("[ SPREADSHEET RECORD INFO ] " + map_name + " no video proof.");
                }
            }

            MapRecord databaseMapRecord;
//            Database record object
            try {
                databaseMapRecord = mapRecordServiceProd.getRecord(map_id, category);
            } catch (NoSuchElementException e){
                System.out.println("[ DATABASE RECORD ERROR ] Couldn't find " + map_name + " in the data, skipping comparison");
                try {
                    SoloMapRecord savedRecord = (SoloMapRecord) mapRecordServiceProd.saveSolo(
                            map_id,
                            map_name,
                            spreadsheetRecordTimeInSeconds,
                            spreadsheetPrevRecordTimeInSeconds,
                            theHero,
                            proof_pic_1_link,
                            proof_pic_2_link,
                            proof_pic_3_link,
                            proof_vid_link,
                            stage_time_1,
                            stage_time_2,
                            stage_time_3);
                } catch (Exception exception) {
                    System.out.println("[ DATABASE RECORD ERROR ] Error adding new record to database: \n" + exception);
                }
                continue;
            }

            int databaseRecordTimeInSeconds;
            try{
                databaseRecordTimeInSeconds = databaseMapRecord.getCurr_wr_seconds();
            } catch (Exception e) {
                System.out.println("[ DATABASE RECORD MESSAGE ] " + map_name + " doesn't have valid WR time. I don't know what to do, skipping... ");
                continue;
            }

            if (spreadsheetRecordTimeInSeconds >= databaseRecordTimeInSeconds) {
                continue;
            }

            System.out.println("[ RECORD BEATEN ] for map " + map_name + ": " + databaseRecordTimeInSeconds + " -> " + spreadsheetRecordTimeInSeconds);
            switch(category){
                case "any":
                    mapRecordServiceProd.updateAny(
                            map_id,
                            map_name,
                            spreadsheetRecordTimeInSeconds,
                            spreadsheetPrevRecordTimeInSeconds,
                            proof_pic_1_link,
                            proof_pic_2_link,
                            proof_pic_3_link,
                            proof_vid_link,
                            stage_time_1,
                            stage_time_2,
                            stage_time_3);
                    break;
                case "solo":
                    SoloMapRecord updatedRecord = (SoloMapRecord) mapRecordServiceProd.updateSolo(
                            map_id,
                            map_name,
                            spreadsheetRecordTimeInSeconds,
                            spreadsheetPrevRecordTimeInSeconds,
                            theHero,
                            proof_pic_1_link,
                            proof_pic_2_link,
                            proof_pic_3_link,
                            proof_vid_link,
                            stage_time_1,
                            stage_time_2,
                            stage_time_3);
                    break;
            }
        }

        System.out.println("[ STATE UPDATE ] Comparing Spreadsheet values to JSON values.. ");

        try{
            JsonNode oldRecords = JSONHandler.readRecordsJSON("records.json");
            JsonNode beatenRecords = JSONHandler.beatenRecords(oldRecords, spreadsheetRecordValues);
            if (!beatenRecords.isEmpty()) {
                System.out.println("[ JSON INFO ] Beaten records: \n" + beatenRecords.toPrettyString());
                JSONHandler.writeRecordsJSON("records.json", spreadsheetRecordValues);
            }
        } catch (IOException e) {
            JSONHandler.writeRecordsJSON("records.json", spreadsheetRecordValues);
        }
    }
}