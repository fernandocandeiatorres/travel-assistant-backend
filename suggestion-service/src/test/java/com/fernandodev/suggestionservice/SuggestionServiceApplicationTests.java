package com.fernandodev.suggestionservice;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import com.fernandodev.suggestionservice.repository.SuggestionRepository;
import com.fernandodev.suggestionservice.repository.ItineraryRepository;

@SpringBootTest
class SuggestionServiceApplicationTests {

	@MockBean
	private RabbitTemplate rabbitTemplate;

	@MockBean
	private SuggestionRepository suggestionRepository;
	
	@MockBean
	private ItineraryRepository itineraryRepository;

	@Test
	void contextLoads() {
	}

}
