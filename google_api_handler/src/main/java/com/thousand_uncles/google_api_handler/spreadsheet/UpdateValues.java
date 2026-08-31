package com.thousand_uncles.google_api_handler.spreadsheet;

import com.google.api.client.googleapis.json.GoogleJsonError;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.UpdateValuesResponse;
import com.google.api.services.sheets.v4.model.ValueRange;
import java.io.IOException;
import java.util.List;

public class UpdateValues {

    private static Sheets service;
    private static String valueInputOption;
    private static final String spreadsheetId = "1YtpbwvqTOiBRN4Sm9SXlNJCq6qPb2dvW3SBmwu5poNs";

    private UpdateValues(){

    }

    public static void setService(Sheets service) {
        UpdateValues.service = service;
    }

    public static void setValueInputOption(String valueInputOption) {
        UpdateValues.valueInputOption = valueInputOption;
    }

    public static void updateSheets(List<List<Object>> values, String sheetName, String topLeft) {
        UpdateValuesResponse result;
        try {
            // Updates the values in the specified range.
            ValueRange body = new ValueRange()
                    .setValues(values);
            result = service.spreadsheets().values().update(spreadsheetId, sheetName+"!" + topLeft, body)
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
    }
}
