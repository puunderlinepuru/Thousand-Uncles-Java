package com.thousand_uncles.data_controller;

import com.thousand_uncles.data.dto.RecordRequestEnvelope;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.AsyncRabbitTemplate;
import org.springframework.amqp.rabbit.RabbitConverterFuture;
import org.springframework.boot.test.context.SpringBootTest;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.node.ObjectNode;

import java.util.concurrent.ExecutionException;

@SpringBootTest
class DataControllerApplicationTests {

	@Mock
	AsyncRabbitTemplate asyncRabbitTemplate;

	private final DirectExchange directMapRecordsExchange = new DirectExchange("map.records.checking");
	public static final String ROUTING_KEY = "map.record";

	@Test
	void contextLoads() {
	}

	@Test
	public void testRabbitExchange() throws Exception {
		ObjectNode message = new ObjectMapper().createObjectNode();

		message.put("map_name", "cp_altitude");
		message.put("category", "any");
		RecordRequestEnvelope requestEnvelope = new RecordRequestEnvelope("1", message);

		RabbitConverterFuture<RecordRequestEnvelope> response = asyncRabbitTemplate.convertSendAndReceive(
				directMapRecordsExchange.getName(),
				ROUTING_KEY,
				requestEnvelope
		);
		try{
//			return response.get();
			System.out.println("response: " + response.get());
		} catch (ExecutionException | InterruptedException e) {
			throw new RuntimeException(e);
		}
	}
}
