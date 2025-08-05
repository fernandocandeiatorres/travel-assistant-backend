package com.fernandodev.suggestionservice.consumer;

import com.fernandodev.suggestionservice.config.RabbitMQConfig;
import com.fernandodev.suggestionservice.dto.SuggestionCreateDTO;
import com.fernandodev.suggestionservice.dto.TripCreatedEvent;
import com.fernandodev.suggestionservice.service.SuggestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class TripEventConsumer {

    private final SuggestionService suggestionService;

    @RabbitListener(queues = RabbitMQConfig.QUEUE_NAME)
    public void consumeCreateTripEvent(TripCreatedEvent event) {
        System.out.println("Evento de criação de viagem recebido para o ID: " + event.tripId());
        System.out.println("Destino: " + event.destination() + " | Usuário: " + event.userId());
        
        suggestionService.generateSuggestionFor(event);
        System.out.println("Sugestões geradas com sucesso para a viagem: " + event.tripId());
    }
}
