package com.thousand_uncles.google_api_handler.app.util;

import com.google.api.client.googleapis.json.GoogleJsonError;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.UpdateValuesResponse;
import com.google.api.services.sheets.v4.model.ValueRange;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

/* Class to demonstrate the use of Spreadsheet Update Values API */
public class UpdateValues {
    /**
     * Sets values in a range of a spreadsheet.
     *
     * @param spreadsheetId    - Id of the spreadsheet.
     * @param range            - Range of cells of the spreadsheet.
     * @param valueInputOption - Determines how input data should be interpreted.
     * @param values           - List of rows of values to input.
     * @return spreadsheet with updated values
     * @throws IOException - if credentials file not found.
     */

    static Sheets service;
    static String valueInputOption;

    public UpdateValues(Sheets service, String valueInputOption){
        UpdateValues.service = service;
        UpdateValues.valueInputOption = valueInputOption;
    }

    public static void updateSheets(String spreadsheetId, List<List<Object>> values) {
        UpdateValuesResponse result = null;
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
    }
}
