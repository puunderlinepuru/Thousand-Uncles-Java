package com.thousand_uncles.discord_bot.config;

import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.AsyncRabbitTemplate;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {
    @Bean
    public DirectExchange directMapRecordsExchange() {
        return new DirectExchange("map.records.checking");
    }

    @Bean
    public JacksonJsonMessageConverter messageConverterFactory() {
        return new JacksonJsonMessageConverter();
    }

    @Bean
    public AsyncRabbitTemplate asyncRabbitTemplate(
            RabbitTemplate rabbitTemplate){
        return new AsyncRabbitTemplate(rabbitTemplate);
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, JacksonJsonMessageConverter messageConverter) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter);
        return rabbitTemplate;
    }
}
