package com.thousand_uncles.data_controller.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {
    @Bean
    public DirectExchange directExchange() {
        return new DirectExchange("map.records.checking");
    }

    @Bean
    public Queue queue() {
        return new Queue("data.controller.requests");
    }

    @Bean
    public DirectExchange discordMessageExchange() {
        return new DirectExchange("map.records.checking");
    }

    @Bean
    public Queue discordMessageQueue() {
        return new Queue("data.controller.requests");
    }

    @SuppressWarnings("unused")
    @Bean
    public Binding discordMessageBinding() {
        return BindingBuilder.bind(discordMessageQueue()).to(discordMessageExchange()).with("map.record");
    }

    @Bean
    public Binding binding(DirectExchange directExchange,
                           Queue queue) {
        return BindingBuilder.bind(queue)
                .to(directExchange)
                .with("map.record");
    }
}
