package com.thousand_uncles.google_api_handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;


//@Component
public class TestComponent {

    RabbitTemplate rabbitTemplate;

    @Autowired
    DirectExchange eventExchange;

    @Autowired
    org.springframework.amqp.core.Queue eventQueue;

    @SuppressWarnings("unused")
    @Autowired
    Binding eventBinding;

    /*@Bean
    public DirectExchange eventExchange() {
        return new DirectExchange("tf2.round.completed");
    }

    @Bean
    public org.springframework.amqp.core.Queue eventQueue() {
        return new Queue("tf2.round.completed");
    }

    @SuppressWarnings("unused")
    @Bean
    public Binding eventBinding() {
        return BindingBuilder.bind(eventQueue()).to(eventExchange()).with("tf2.round.completed");
    }*/

    TestComponent(RabbitTemplate rabbitTemplate){
        this.rabbitTemplate = rabbitTemplate;
        System.out.println("init");
        sendTest();
    }

    private void sendTest(){
        try{
            ObjectMapper objectMapper = new ObjectMapper();
//            ObjectNode objectNode = objectMapper.valueToTree();
//            objectNode.put("category", category);
//            String jsonString = objectMapper.writeValueAsString(objectNode);
            String jsonString = "{\"schema_version\":1,\"record_type\":\"tf2_game_event\",\"id\":\"172.30.0.1:27015:000000000068\",\"server_address\":\"172.30.0.1:27015\",\"server_name\":\"Local TF2 Plugin Test\",\"round_id\":\"172.30.0.1:27015:cp_well:2026-07-07T18:18:03+03:00:pending:0009\",\"round_start_timestamp\":\"2026-07-07T18:18:03+03:00\",\"sequence\":68,\"logged_at\":\"2026-07-07T18:25:13+03:00\",\"map\":{\"name\":\"cp_well\"},\"event_class\":\"forward_call_event\",\"event_source\":\"SourceModCore\",\"event_name\":\"OnMapEnd\",\"data\":{\"next_map\":\"cp_egypt_final\",\"has_next_map\":true}}";
            rabbitTemplate.convertAndSend("event.exchange", "event.routing.key", jsonString);
            System.out.println("sent");
        }catch (Exception e) {
            System.out.println("error: " + e);
        }
    }
}
