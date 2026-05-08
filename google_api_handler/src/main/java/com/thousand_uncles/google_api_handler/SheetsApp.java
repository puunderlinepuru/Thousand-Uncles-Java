package com.thousand_uncles.google_api_handler;

import com.thousand_uncles.google_api_handler.app.GoogleAPI_Handler;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import java.io.IOException;
import java.security.GeneralSecurityException;

@SpringBootApplication
public class SheetsApp {
    public static void main(String[] args) {
        try {
            ApplicationContext applicationContext = SpringApplication.run(SheetsApp.class, args);
            GoogleAPI_Handler googleAPIHandler = applicationContext.getBean(GoogleAPI_Handler.class);
            try {
                googleAPIHandler.startTasks();
            } catch (GeneralSecurityException | IOException e) {
                throw new RuntimeException(e);
            }
        } catch (BeanCreationException e) {
            System.out.println("blep");
//            throw new RuntimeException(e);
        }
    }
}
