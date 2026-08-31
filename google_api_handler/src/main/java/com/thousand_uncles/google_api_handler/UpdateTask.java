package com.thousand_uncles.google_api_handler;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.ValueRange;
import com.thousand_uncles.data.models.uncletopia.AnyPercentMapRecordEntry;
import com.thousand_uncles.data.models.common.ManualIndexedMapRecordEntry;
import com.thousand_uncles.data.models.uncletopia.SoloMapRecordEntry;
import com.thousand_uncles.data.service.MapRecordServiceProd;
import com.thousand_uncles.data.util.RecordFormatter;
import com.thousand_uncles.google_api_handler.services.SpreadsheetsService;
import com.thousand_uncles.google_api_handler.util.GlobalThings;
import com.thousand_uncles.google_api_handler.util.JSONHandler;
import org.springframework.context.ApplicationContext;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

public class UpdateTask extends TimerTask {
    String valueInputOption;
    Sheets service;
    String spreadsheetId;

    ApplicationContext applicationContext;

    SpreadsheetsService spreadsheetsService;

    public UpdateTask(String spreadSheetId, String valueInputOption, Sheets service, ApplicationContext applicationContext, SpreadsheetsService spreadsheetsService){
        this.spreadsheetId = spreadSheetId;
        this.valueInputOption = valueInputOption;
        this.service = service;
        this.applicationContext = applicationContext;
        this.spreadsheetsService = spreadsheetsService;
    }

    public void run() {
        String category;
        List<List<String>> spreadsheetRecordValues;

//        Uncletopia Any%
        spreadsheetRecordValues = cleanSheetData(readSheets("UncletopiaAny%", "A3", "G69"));
        category = "any";
        System.out.println("[ STATE CHANGE ] Checking Uncletopia Any%");
        spreadsheetsService.processAnyPercentSheetData(spreadsheetRecordValues, category);

//        RUN Any%
        System.out.println("[ STATE CHANGE ] Checking RUN Any%");
        spreadsheetRecordValues = cleanSheetData(readSheets("RUNAny%", "A3", "G120"));
        category = "run_any";
        System.out.println("[ STATE CHANGE ] Comparing spreadsheet records to database.. ");
        spreadsheetsService.processAnyPercentSheetData(spreadsheetRecordValues, category);


//        Uncletopia Solo%
        spreadsheetRecordValues = cleanSheetData(readSheets("UncletopiaSolo%", "A3", "H64"));
        category = "solo";
        System.out.println("[ STATE CHANGE ] Checking Uncletopia Solo%");
        spreadsheetsService.processSoloSheetData(spreadsheetRecordValues, category);

//        RUN Solo%
        System.out.println("[ STATE CHANGE ] Checking RUN Solo%");
        spreadsheetRecordValues = cleanSheetData(readSheets("RUNSolo%", "A3", "G121"));
        category = "run_solo";
        System.out.println("[ STATE CHANGE ] Comparing spreadsheet records to database.. ");
        spreadsheetsService.processSoloSheetData(spreadsheetRecordValues, category);

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
}