package com.thousand_uncles.google_api_handler;

import com.google.api.client.util.Value;
import com.thousand_uncles.google_api_handler.app.GoogleAPI_Handler;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.jdbc.autoconfigure.DataSourceAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Arrays;

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

    @Bean
    public DirectExchange testExchange() {
        return new DirectExchange("test.exchange");
    }

    @Bean
    public Queue testQueue() {
        return new Queue("test.queue");
    }

    @Bean
    public Binding testBinding() {
        return BindingBuilder.bind(testQueue()).to(testExchange()).with("test.routing.key");
    }
}
