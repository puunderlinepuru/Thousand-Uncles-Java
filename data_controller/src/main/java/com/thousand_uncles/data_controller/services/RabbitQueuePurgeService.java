package com.thousand_uncles.data_controller.services;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RabbitQueuePurgeService {

    @Autowired
    RabbitTemplate rabbitTemplate;

    public RabbitQueuePurgeService() {
    }

    public void purgeQueue(String queueName) {
        try {
            rabbitTemplate.execute((channel) -> {
                channel.queuePurge(queueName);
                System.out.println("Queue purged");
                return null;
            });
        } catch (Exception e) {
            System.out.println("Failed to purge queue: {}" + e.getMessage());
        }
    }
}

