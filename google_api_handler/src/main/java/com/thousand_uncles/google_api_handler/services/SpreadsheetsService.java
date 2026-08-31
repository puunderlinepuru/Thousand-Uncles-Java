package com.thousand_uncles.google_api_handler.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.thousand_uncles.data.models.common.ManualIndexedMapRecordEntry;
import com.thousand_uncles.data.models.run.RunAnyPercentMapRecordEntry;
import com.thousand_uncles.data.models.run.RunSoloMapRecordEntry;
import com.thousand_uncles.data.models.uncletopia.AnyPercentMapRecordEntry;
import com.thousand_uncles.data.models.uncletopia.SoloMapRecordEntry;
import com.thousand_uncles.data.service.MapRecordServiceProd;
import com.thousand_uncles.data.util.RecordFormatter;
import com.thousand_uncles.google_api_handler.util.GlobalThings;
import com.thousand_uncles.google_api_handler.util.JSONHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

@Component
public class SpreadsheetsService {

    @Autowired
    MapRecordServiceProd mapRecordServiceProd;

    public void processAnyPercentSheetData(List<List<String>> spreadsheetRecordValues, String category){
        for (int i = 0; i < spreadsheetRecordValues.size(); i++) {
//            Name
            String map_name;
            try {
                map_name = spreadsheetRecordValues.get(i).getFirst();
            } catch (Exception e) {
                System.out.println("row stars blank, skipping ");
                continue;
            }

//            getId(mapName)
            int map_id = GlobalThings.getMapIDS().indexOf(map_name);
            if (map_id == -1){
                System.out.println(" couldn't find index for map, skipping");
                continue;
            }

//            Current WR
            BigDecimal spreadsheetRecordTimeInSeconds;
            try {
                spreadsheetRecordTimeInSeconds = RecordFormatter.StringToBigDecimal(spreadsheetRecordValues.get(i).get(1));
            } catch (NumberFormatException e){
                System.out.println("[ SPREADSHEET RECORD ERROR ] Issue with WR number format for map " + map_name + ". String in spreadsheets: " + spreadsheetRecordValues.get(i).get(2) + ". Skipping..");
                continue;
            }

//            Previous WR
            BigDecimal spreadsheetPrevRecordTimeInSeconds = BigDecimal.ZERO;
            try {
                spreadsheetPrevRecordTimeInSeconds = RecordFormatter.StringToBigDecimal(spreadsheetRecordValues.get(i).get(2));
            } catch (NumberFormatException e){
                System.out.println("[ SPREADSHEET RECORD ERROR ] Issue with previous WR number for map " + map_name + ". String in spreadsheets: " + spreadsheetRecordValues.get(i).get(2) + ". Skipping..");
            }

            String proof_pic_1_link,
                    proof_pic_2_link = null,
                    proof_pic_3_link = null,
                    proof_vid_link = null;
            BigDecimal stage_time_1 = null,
                    stage_time_2 = null,
                    stage_time_3 = null;

            if (spreadsheetRecordValues.get(i).get(3).isEmpty()){
                System.out.println("[ SPREADSHEET RECORD ERROR ] Proof image field is empty for map " + map_name + ", skipping");
                continue;
            }

//            Stage times for multistage maps
            if (spreadsheetRecordValues.get(i).get(3).charAt(0) != 'h')
            {
//                System.out.println("[ SPREADSHEET RECORD INFO ] Not a link detected, processing as stage times. ");
                try{
                    stage_time_1 = RecordFormatter.StringToBigDecimal(spreadsheetRecordValues.get(i).get(3));
                    stage_time_2 = RecordFormatter.StringToBigDecimal(spreadsheetRecordValues.get(i).get(4));
                } catch (Exception e) {
                    System.out.println("Issue with times of basic 2 stages");
                }

                try{
                    stage_time_3 = RecordFormatter.StringToBigDecimal(spreadsheetRecordValues.get(i).get(5));
                } catch (Exception exception){
                    System.out.println("multistage map probably doesn't have 3 stages");
                }

                try {
                    proof_vid_link = spreadsheetRecordValues.get(i).get(6);
                } catch (Exception e) {
                    System.out.println("[ SPREADSHEET RECORD INFO ] " + map_name + " no video proof");
                }

//                Moving to the next line
                i++;

                try{
                    proof_pic_1_link = spreadsheetRecordValues.get(i).get(3);
                    proof_pic_2_link = spreadsheetRecordValues.get(i).get(4);
                } catch (Exception e) {
                    System.out.println("Issue with proofs for multistage map");
                    continue;
                }

                try{
                    proof_pic_3_link = spreadsheetRecordValues.get(i).get(5);
                } catch (Exception exception){
                    System.out.println("multistage map probably doesn't have 3 stages");
                }
            } else {
                proof_pic_1_link = spreadsheetRecordValues.get(i).get(3);
                try {
                    proof_vid_link = spreadsheetRecordValues.get(i).get(4);
                } catch (Exception e) {
                    System.out.println("[ SPREADSHEET RECORD INFO ] " + map_name + " no video proof.");
                }
            }

            ManualIndexedMapRecordEntry databaseMapRecord;
            databaseMapRecord = mapRecordServiceProd.getRecord(map_id, category);
            if (databaseMapRecord == null){
                System.out.println("[ DATABASE RECORD ERROR ] Couldn't find " + map_name + " in the data, skipping comparison");
                try {
                    if (Objects.equals(category, "any")){
                        AnyPercentMapRecordEntry savedRecord = (AnyPercentMapRecordEntry) mapRecordServiceProd.saveUncletopiaAny(
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
                    } else if(Objects.equals(category, "run_any")){
                        RunAnyPercentMapRecordEntry savedRecord = (RunAnyPercentMapRecordEntry) mapRecordServiceProd.saveRunAny(
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
                } catch (Exception exception) {
                    System.out.println("[ DATABASE RECORD ERROR ] Error adding new record to database: \n" + exception);
                }
                continue;
            }

            BigDecimal databaseRecordTimeInSeconds;
            try{
                databaseRecordTimeInSeconds = databaseMapRecord.getCurr_wr_seconds();
            } catch (Exception exception) {
                System.out.println("[ DATABASE RECORD MESSAGE ] " + map_name + " Error reading: \n" + exception);
                continue;
            }

            if (spreadsheetRecordTimeInSeconds.compareTo(databaseRecordTimeInSeconds) >= 0) {
                continue;
            }

            System.out.println("[ RECORD BEATEN ] for map " + map_name + ": " + databaseRecordTimeInSeconds + " -> " + spreadsheetRecordTimeInSeconds);
            try {
                if (Objects.equals(category, "any")){
                    AnyPercentMapRecordEntry savedRecord = (AnyPercentMapRecordEntry) mapRecordServiceProd.saveUncletopiaAny(
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
                } else if(Objects.equals(category, "run_any")){
                    RunAnyPercentMapRecordEntry savedRecord = (RunAnyPercentMapRecordEntry) mapRecordServiceProd.saveRunAny(
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
            } catch (Exception exception) {
                System.out.println("[ DATABASE RECORD ERROR ] Error adding new record to database: \n" + exception);
            }
        }

        /*System.out.println("[ STATE UPDATE ] Comparing Spreadsheet values to JSON values.. ");

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
        }*/
    }

    public void processSoloSheetData(List<List<String>> spreadsheetRecordValues, String category){
        for (int i = 0; i < spreadsheetRecordValues.size(); i++) {

//            Name
            String map_name;
            try {
                map_name = spreadsheetRecordValues.get(i).getFirst().split(" ")[0];
            } catch (Exception e) {
                System.out.println("row stars blank, skipping ");
                continue;
            }


            int map_id = GlobalThings.getMapIDS().indexOf(map_name);
            if (map_id == -1){
                System.out.println(" couldn't find index for map " + map_name + ", skipping");
                continue;
            }


//            Current WR
            BigDecimal spreadsheetRecordTimeInSeconds;
            try {
                spreadsheetRecordTimeInSeconds = RecordFormatter.StringToBigDecimal(spreadsheetRecordValues.get(i).get(1));
            } catch (NumberFormatException e){
                System.out.println("[ SPREADSHEET RECORD ERROR ] Issue with WR number format for map " + map_name + ". String in spreadsheets: " + spreadsheetRecordValues.get(i).get(2) + ". Skipping..");
                continue;
            }


//            Previous WR
            BigDecimal spreadsheetPrevRecordTimeInSeconds = BigDecimal.ZERO;
            try {
                spreadsheetPrevRecordTimeInSeconds = RecordFormatter.StringToBigDecimal(spreadsheetRecordValues.get(i).get(2));
            } catch (NumberFormatException e){
                System.out.println("[ SPREADSHEET RECORD ERROR ] Issue with previous WR number for map " + map_name + ". String in spreadsheets: " + spreadsheetRecordValues.get(i).get(2) + ".");
            }

            String proof_pic_1_link;
            String proof_pic_2_link = null;
            String proof_pic_3_link = null;
            String proof_vid_link = null;
            BigDecimal stage_time_1 = null;
            BigDecimal stage_time_2 = null;
            BigDecimal stage_time_3 = null;

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
                try {
                    stage_time_1 = RecordFormatter.StringToBigDecimal(spreadsheetRecordValues.get(i).get(4));
                } catch (Exception e){
                    System.out.println("no proof provided, skipping ");
                    continue;
                }
                try{
                    stage_time_2 = RecordFormatter.StringToBigDecimal(spreadsheetRecordValues.get(i).get(5));
                } catch (Exception exception){
                    System.out.println("failure on 2nd proof processing");
                }

                try{
                    stage_time_3 = RecordFormatter.StringToBigDecimal(spreadsheetRecordValues.get(i).get(6));
                } catch (Exception exception){
                    System.out.println("failure on 3nd proof processing");
                }

                try {
                    proof_vid_link = spreadsheetRecordValues.get(i).get(7);
                } catch (Exception e) {
                    System.out.println("[ SPREADSHEET RECORD INFO ] " + map_name + " no video proof");
                }

//                Moving to the next line
                i++;

                try {
                    proof_pic_1_link = spreadsheetRecordValues.get(i).get(4);
                } catch (Exception e) {
                    System.out.println("doesn't have any sort of proof, skipping..");
                    continue;
                }

                try{
                    proof_pic_2_link = spreadsheetRecordValues.get(i).get(5);
                    proof_pic_3_link = spreadsheetRecordValues.get(i).get(6);
                } catch (Exception e) {
                    System.out.println("record doesn't have image proofs");
                }
            } else {
                proof_pic_1_link = spreadsheetRecordValues.get(i).get(4);
                try {
                    proof_vid_link = spreadsheetRecordValues.get(i).get(5);
                } catch (Exception e) {
                    System.out.println("[ SPREADSHEET RECORD INFO ] " + map_name + " no video proof.");
                }
            }

            ManualIndexedMapRecordEntry databaseMapRecord;
            databaseMapRecord = mapRecordServiceProd.getRecord(map_id, category);
            if (databaseMapRecord == null){
                System.out.println("[ DATABASE RECORD ERROR ] Couldn't find " + map_name + " in the data, skipping comparison");
                try {
                    if (Objects.equals(category, "solo")){
                        SoloMapRecordEntry savedRecord = (SoloMapRecordEntry) mapRecordServiceProd.saveUncletopiaSolo(
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
                    } else if (Objects.equals(category, "run_solo")){
                        RunSoloMapRecordEntry savedRecord = (RunSoloMapRecordEntry) mapRecordServiceProd.saveRunSolo(
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
                    }
                } catch (Exception exception) {
                    System.out.println("[ DATABASE RECORD ERROR ] Error adding new record to database: \n" + exception);
                }
                continue;
            }

            BigDecimal databaseRecordTimeInSeconds;
            try{
                databaseRecordTimeInSeconds = databaseMapRecord.getCurr_wr_seconds();
            } catch (Exception e) {
                System.out.println("[ DATABASE RECORD MESSAGE ] " + map_name + " doesn't have valid WR time. I don't know what to do, skipping... ");
                continue;
            }

            if (spreadsheetRecordTimeInSeconds.compareTo(databaseRecordTimeInSeconds) >= 0) {
                continue;
            }

            System.out.println("[ RECORD BEATEN ] for map " + map_name + ": " + databaseRecordTimeInSeconds + " -> " + spreadsheetRecordTimeInSeconds);
            try {
                if (Objects.equals(category, "solo")){
                    SoloMapRecordEntry savedRecord = (SoloMapRecordEntry) mapRecordServiceProd.saveUncletopiaSolo(
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
                } else if (Objects.equals(category, "run_solo")){
                    RunSoloMapRecordEntry savedRecord = (RunSoloMapRecordEntry) mapRecordServiceProd.saveRunSolo(
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
                }
            } catch (Exception exception) {
                System.out.println("[ DATABASE RECORD ERROR ] Error adding new record to database: \n" + exception);
            }
        }

        /*System.out.println("[ STATE UPDATE ] Comparing Spreadsheet values to JSON values.. ");

        try{
            JsonNode oldRecords = JSONHandler.readRecordsJSON("records.json");
            JsonNode beatenRecords = JSONHandler.beatenRecords(oldRecords, spreadsheetRecordValues);
            if (!beatenRecords.isEmpty()) {
                System.out.println("[ JSON INFO ] Beaten records: \n" + beatenRecords.toPrettyString());
                JSONHandler.writeRecordsJSON("records.json", spreadsheetRecordValues);
            }
        } catch (IOException e) {
            JSONHandler.writeRecordsJSON("records.json", spreadsheetRecordValues);
        }*/
    }
}
