package com.thousand_uncles.discord_bot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DiscordBotApplication {

	public static void main(String[] args) {
        YamlReader configReader = new YamlReader("config.yaml");
		SpringApplication.run(DiscordBotApplication.class, args);
        System.out.println(configReader.yamlRead());

	}

}
