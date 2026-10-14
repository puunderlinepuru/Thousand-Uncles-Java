package com.thousand_uncles.google_api_handler;

import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.ValueRange;
import com.thousand_uncles.google_api_handler.config.GAPIConfig;
import com.thousand_uncles.google_api_handler.services.SpreadsheetsService;
import org.springframework.context.ApplicationContext;

import java.io.IOException;
import java.util.*;

public class UpdateTask extends TimerTask {
    String valueInputOption;
    Sheets service;
    String spreadsheetId;

    ApplicationContext applicationContext;

    SpreadsheetsService spreadsheetsService;

    GAPIConfig.Sheets gapiConfigSheets;

    public UpdateTask(String spreadSheetId, String valueInputOption, Sheets service, ApplicationContext applicationContext, SpreadsheetsService spreadsheetsService, GAPIConfig.Sheets gapiConfigSheets){
        this.spreadsheetId = spreadSheetId;
        this.valueInputOption = valueInputOption;
        this.service = service;
        this.applicationContext = applicationContext;
        this.spreadsheetsService = spreadsheetsService;
        this.gapiConfigSheets = gapiConfigSheets;
    }

    public void run() {
        String category;
        List<List<String>> spreadsheetRecordValues;

        char leftValuesBorder = 'A';
        char rightValuesBorder = 'G';

        GAPIConfig.Sheets.Uncletopia_Any configUncletopiaAny = gapiConfigSheets.getUncletopia_any();
        GAPIConfig.Sheets.Uncletopia_Cheeseless configUncletopiaCheeseless = gapiConfigSheets.getUncletopia_cheeseless();
        GAPIConfig.Sheets.Uncletopia_Solo configUncletopiaSolo = gapiConfigSheets.getUncletopia_solo();

        GAPIConfig.Sheets.Run_Any configRunAny = gapiConfigSheets.getRun_any();
        GAPIConfig.Sheets.Run_Cheeseless configRunCheeseless = gapiConfigSheets.getRun_cheeseless();
        GAPIConfig.Sheets.Run_Solo configRunSolo = gapiConfigSheets.getRun_solo();

//        Uncletopia Any%
        System.out.println("[ STATE CHANGE ] Checking Uncletopia Any% " +
                leftValuesBorder + String.valueOf(configUncletopiaAny.getBegins()) +
                "  |" +
                rightValuesBorder + String.valueOf(configUncletopiaAny.getEnds()));
        spreadsheetRecordValues = cleanSheetData(
                readSheets(
                    "UncletopiaAny%",
                        leftValuesBorder + String.valueOf(configUncletopiaAny.getBegins()),
                        rightValuesBorder + String.valueOf(configUncletopiaAny.getEnds())
                )
        );
        category = "any";
        System.out.println("[ STATE CHANGE ] Comparing spreadsheet records to database.. ");
        spreadsheetsService.processAnyPercentSheetData(spreadsheetRecordValues, category);

//        RUN Any%
        System.out.println("[ STATE CHANGE ] Checking RUN Any%" +
                leftValuesBorder + String.valueOf(configRunAny.getBegins()) +
                "|" +
                rightValuesBorder + String.valueOf(configRunAny.getEnds()));
        spreadsheetRecordValues = cleanSheetData(
                readSheets(
                        "RUNAny%",
                        leftValuesBorder + String.valueOf(configRunAny.getBegins()),
                        rightValuesBorder + String.valueOf(configRunAny.getEnds())
                )
        );
        category = "run_any";
        System.out.println("[ STATE CHANGE ] Comparing spreadsheet records to database.. ");
        spreadsheetsService.processAnyPercentSheetData(spreadsheetRecordValues, category);

//        Uncletopia Cheeseless%
        System.out.println("[ STATE CHANGE ] Checking Uncletopia Cheeseless%" +
                leftValuesBorder + String.valueOf(configUncletopiaCheeseless.getBegins()) +
                "|" +
                rightValuesBorder + String.valueOf(configUncletopiaCheeseless.getEnds()));
        spreadsheetRecordValues = cleanSheetData(
                readSheets(
                        "UncletopiaCheeseless%",
                        leftValuesBorder + String.valueOf(configUncletopiaCheeseless.getBegins()),
                        rightValuesBorder + String.valueOf(configUncletopiaCheeseless.getEnds())
                )
        );
        category = "cheeseless";
        System.out.println("[ STATE CHANGE ] Comparing spreadsheet records to database.. ");
//        TODO Implement (processCheeselessSheetData doesn't exist
//        spreadsheetsService.processCheeselessSheetData(spreadsheetRecordValues, category);

//        RUN Cheeseless%
        System.out.println("[ STATE CHANGE ] Checking RUN Cheeseless%" +
                leftValuesBorder + String.valueOf(configRunCheeseless.getBegins()) +
                "|" +
                rightValuesBorder + String.valueOf(configRunCheeseless.getEnds()));
        spreadsheetRecordValues = cleanSheetData(
                readSheets(
                        "RUNCheeseless%",
                        leftValuesBorder + String.valueOf(configRunCheeseless.getBegins()),
                        rightValuesBorder + String.valueOf(configRunCheeseless.getEnds())
                )
        );
        category = "run_cheeseless";
        System.out.println("[ STATE CHANGE ] Comparing spreadsheet records to database.. ");
//        TODO Implement (processCheeselessSheetData doesn't exist
//        spreadsheetsService.processCheeselessSheetData(spreadsheetRecordValues, category);

//        Uncletopia Solo%
        System.out.println("[ STATE CHANGE ] Checking Uncletopia Solo%" +
                leftValuesBorder + String.valueOf(configUncletopiaSolo.getBegins()) +
                "|" +
                rightValuesBorder + String.valueOf(configUncletopiaSolo.getEnds()));
        spreadsheetRecordValues = cleanSheetData(
                readSheets(
                        "UncletopiaSolo%",
                        leftValuesBorder + String.valueOf(configUncletopiaSolo.getBegins()),
                        rightValuesBorder + String.valueOf(configUncletopiaSolo.getEnds())
                )
        );
        category = "solo";
        System.out.println("[ STATE CHANGE ] Comparing spreadsheet records to database.. ");
        spreadsheetsService.processSoloSheetData(spreadsheetRecordValues, category);

//        RUN Solo%
        System.out.println("[ STATE CHANGE ] Checking RUN Solo%" +
                leftValuesBorder + String.valueOf(configRunSolo.getBegins()) +
                "|" +
                rightValuesBorder + String.valueOf(configRunSolo.getEnds()));
        spreadsheetRecordValues = cleanSheetData(
                readSheets(
                        "RUNSolo%",
                        leftValuesBorder + String.valueOf(configRunSolo.getBegins()),
                        rightValuesBorder + String.valueOf(configRunSolo.getEnds())
                )
        );
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