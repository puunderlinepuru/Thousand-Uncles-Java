package com.thousand_uncles.google_api_handler.app;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.api.client.googleapis.json.GoogleJsonError;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.UpdateValuesResponse;
import com.google.api.services.sheets.v4.model.ValueRange;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.TimerTask;

class Update_Task extends TimerTask {
    UpdateValuesResponse result;
    String valueInputOption;
    Sheets service;
    String spreadsheetId;
    List<List<Object>> valuesToPost;

    Update_Task(String spreadSheetId, UpdateValuesResponse result, String valueInputOption, List<List<Object>> valuesToPost, Sheets service){
        this.spreadsheetId = spreadSheetId;
        this.result = result;
        this.valueInputOption = valueInputOption;
        this.valuesToPost = valuesToPost;
        this.service = service;
    }
    public void run() {
        List<List<Object>> values;

//        Any%
        values = readSheets("Any%", "A2", "G53");
//        load old json
//        compare new to old
//        write or not write
//        updateSheets();
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