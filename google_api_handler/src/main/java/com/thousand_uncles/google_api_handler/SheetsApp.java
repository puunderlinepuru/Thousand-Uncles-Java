package com.thousand_uncles.google_api_handler;

import com.thousand_uncles.google_api_handler.app.spreadsheet.GoogleAPI_Handler;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.jdbc.autoconfigure.DataSourceAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import java.io.IOException;
import java.security.GeneralSecurityException;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class SheetsApp {
    public static void main(String[] args) {
        try {
            ApplicationContext applicationContext = SpringApplication.run(SheetsApp.class, args);
            GoogleAPI_Handler googleAPIHandler = applicationContext.getBean(GoogleAPI_Handler.class);
            Environment environment = applicationContext.getEnvironment();
            System.out.println("Profile: " + environment.getProperty("spring.profiles.active"));
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
