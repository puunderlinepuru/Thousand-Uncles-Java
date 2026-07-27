package com.thousand_uncles.dashboard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.jdbc.autoconfigure.DataSourceAutoConfiguration;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.context.ApplicationContext;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.Map;

@SpringBootApplication(
		exclude = {DataSourceAutoConfiguration.class},
		scanBasePackages = {
				"com.thousand_uncles.dashboard",
				"com.thousand_uncles.data"
		})
@EntityScan("com.thousand_uncles.data.models")
@EnableJpaRepositories(basePackages = "com.thousand_uncles.data")
public class DashboardApplication {

	public static void main(String[] args) {
		ApplicationContext context = SpringApplication.run(DashboardApplication.class, args);

//		MapRecordUtil util = context.getBean(MapRecordUtil.class);
//		util.doSomething();
	}
}
