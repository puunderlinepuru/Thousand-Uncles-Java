package com.thousand_uncles.discord_bot.bot.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;

public class RabbitConfig {

    @Bean
    public DirectExchange eventExchange() {
        return new DirectExchange("event.exchange");
    }

    @Bean
    public org.springframework.amqp.core.Queue eventQueue() {
        return new Queue("event.queue");
    }

    @SuppressWarnings("unused")
    @Bean
    public Binding eventBinding() {
        return BindingBuilder.bind(eventQueue()).to(eventExchange()).with("event.routing.key");
    }
}
