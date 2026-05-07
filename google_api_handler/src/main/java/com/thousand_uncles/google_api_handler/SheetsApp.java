package com.thousand_uncles.google_api_handler;

import com.thousand_uncles.google_api_handler.app.GoogleAPI_Handler;
import org.postgresql.util.PSQLException;
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
            //        MapRecordUtil util = applicationContext.getBean(MapRecordUtil.class);
            //        util.doSomething();
            try {
                googleAPIHandler.startTasks();
            } catch (GeneralSecurityException | IOException e) {
                throw new RuntimeException(e);
            }
        } catch (Exception e) {
            System.out.println("blep");
            throw new RuntimeException(e);
        }





    }

    /*@Bean
    public CommandLineRunner commandLineRunner(MapRecordService service) {
        return args -> {
            var records = service.getAllRecords();
            System.out.println("Total records at startup: " + records.size());

            var record = service.getRecordByName("test");
            System.out.println("Found record: " + record);
        };
    }*/
}
