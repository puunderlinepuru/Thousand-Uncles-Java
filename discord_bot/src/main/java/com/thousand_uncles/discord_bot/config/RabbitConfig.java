package com.thousand_uncles.discord_bot.config;

import org.springframework.amqp.core.DirectExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {
    @Bean
    public DirectExchange directMapRecordsExchange() {
        return new DirectExchange("map.records.checking");
    }
}
