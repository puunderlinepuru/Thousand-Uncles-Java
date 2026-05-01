package com.thousand_uncles.dashboard;

import com.thousand_uncles.dashboard.data.util.MapRecordUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class DashboardApplication {
	public static void main(String[] args) {
		ApplicationContext context = SpringApplication.run(DashboardApplication.class, args);

		MapRecordUtil util = context.getBean(MapRecordUtil.class);
		util.doSomething();
	}
}
