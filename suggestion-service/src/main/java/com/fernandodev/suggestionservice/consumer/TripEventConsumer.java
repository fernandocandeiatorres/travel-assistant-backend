package com.fernandodev.suggestionservice.consumer;

import com.fernandodev.suggestionservice.config.RabbitMQConfig;
import com.fernandodev.suggestionservice.dto.SuggestionCreateDTO;
import com.fernandodev.suggestionservice.service.SuggestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class TripEventConsumer {

    private SuggestionService suggestionService;

    @RabbitListener(queues = RabbitMQConfig.QUEUE_NAME)
    public void consumeCreateTripEvent(UUID tripId) {
        System.out.println("Evento de criação de viagem recebido para o ID: " + tripId);
        suggestionService.generateSuggestionFor(tripId);
        System.out.println("Sugestões geradas com sucesso para a viagem: " + tripId);
    }
}
