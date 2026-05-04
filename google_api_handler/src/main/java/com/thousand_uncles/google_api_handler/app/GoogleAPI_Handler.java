package com.thousand_uncles.google_api_handler.app;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.UpdateValuesResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.*;
import java.security.GeneralSecurityException;
import java.util.*;

@Component
public class GoogleAPI_Handler {

    @Autowired
    ApplicationContext applicationContext;

    private static final String APPLICATION_NAME = "Google Sheets API Java Quickstart";
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private static final String TOKENS_DIRECTORY_PATH = "tokens";

    /**
     * Global instance of the scopes required by this quickstart.
     * If modifying these scopes, delete your previously saved tokens/ folder.
     */
    private static final List<String> SCOPES =
            Collections.singletonList(SheetsScopes.SPREADSHEETS);
    private static final String CREDENTIALS_FILE_PATH = "/GoogleAPI_Desktop_secret.json";

    /**
     * Creates an authorized Credential object.
     *
     * @param HTTP_TRANSPORT The network HTTP Transport.
     * @return An authorized Credential object.
     * @throws IOException If the credentials.json file cannot be found.
     */
    private static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT)
            throws IOException {
        // Load client secrets.
        InputStream in = GoogleAPI_Handler.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
        if (in == null) {
            throw new FileNotFoundException("Credentials resource not found: " + CREDENTIALS_FILE_PATH);
        }
        GoogleClientSecrets clientSecrets =
                GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .build();
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }

    public void startTasks() throws IOException, GeneralSecurityException {

//        Show_Directories_And_Files.printFiles();

        // Build a new authorized API client service.
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
//        final String testSpreadsheetID = "1a09PuBN2hnJ58c8km_la3e0_sUAjQ8HatalX7fdMl50";

//        https://docs.google.com/spreadsheets/d/1cxxyzz0SDWCj8wI6QwN66SOtbpedrf2DWXrH6io2ZNk/edit?gid=0#gid=0
        final String uncletopiaSpreadsheetID = "1cxxyzz0SDWCj8wI6QwN66SOtbpedrf2DWXrH6io2ZNk"; // Actual

//        https://docs.google.com/spreadsheets/d/11IRxK5JLbdaUgrMtSZrFQl_EjvdoKTuq4xWV_qPau7s
//        final String uncletopiaSpreadsheetID = "11IRxK5JLbdaUgrMtSZrFQl_EjvdoKTuq4xWV_qPau7s"; // Backup copy from Nov 17

        List<List<Object>> valuesToPost = new ArrayList<>();
        String[] inletValues = {"Another update"};
        valuesToPost.add(List.of(inletValues));

        Sheets service =
                new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                        .setApplicationName(APPLICATION_NAME)
                        .build();

        UpdateValuesResponse result = null;
        String valueInputOption = "RAW";

        Timer timer = new Timer();
        TimerTask timerTask = new UpdateTask(uncletopiaSpreadsheetID, result, valueInputOption, valuesToPost, service, applicationContext);
        timer.schedule(timerTask, 200, 1800000);
    }
}