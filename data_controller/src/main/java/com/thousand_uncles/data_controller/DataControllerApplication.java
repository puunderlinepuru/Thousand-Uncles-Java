package com.thousand_uncles.data_controller;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.jdbc.autoconfigure.DataSourceAutoConfiguration;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(
		exclude = {DataSourceAutoConfiguration.class},
		scanBasePackages = {
				"com.thousand_uncles.data_controller",
				"com.thousand_uncles.data"
		})
@EntityScan("com.thousand_uncles.data.models")
@EnableJpaRepositories(basePackages = "com.thousand_uncles.data")
public class DataControllerApplication {

	public static void main(String[] args) {
		SpringApplication.run(DataControllerApplication.class, args);
	}

}
