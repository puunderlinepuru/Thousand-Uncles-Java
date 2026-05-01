package com.thousand_uncles.google_api_handler.app;

import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class SheetsApp {
    public static void main(String[] args) throws GeneralSecurityException, IOException {
//        SpringApplication.run(SheetsApp.class);
        GoogleAPI_Handler googleAPIHandler = new GoogleAPI_Handler();
    }
}
