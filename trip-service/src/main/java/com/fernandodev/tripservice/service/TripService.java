package com.fernandodev.tripservice.service;


import com.fernandodev.tripservice.config.RabbitMQConfig;
import com.fernandodev.tripservice.dto.TripCreateRequest;
import com.fernandodev.tripservice.dto.TripCreateResponse;
import com.fernandodev.tripservice.dto.TripCreatedEvent;
import com.fernandodev.tripservice.dto.TripGetResponse;
import com.fernandodev.tripservice.dto.TripUpdateRequest;
import com.fernandodev.tripservice.exception.TripNotFoundException;
import com.fernandodev.tripservice.model.Trip;
import com.fernandodev.tripservice.repository.TripRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class TripService {

    private final TripRepository tripRepository;
    private final RabbitTemplate rabbitTemplate;
    private final RestTemplate restTemplate;
    private static final Logger logger = LoggerFactory.getLogger(TripService.class);

    @CircuitBreaker(name = "suggestionService", fallbackMethod = "suggestionServiceFallback")
    public String checkSuggestionServiceHealth() {
        // O nome 'suggestion-service' deve ser resolvível pelo service discovery (ou Docker DNS)
        return restTemplate.getForObject("http://suggestion-service:8080/api/v1/suggestions/health", String.class);
    }

    public String suggestionServiceFallback(Throwable t) {
        logger.warn("Suggestion service is down. Fallback triggered.", t);
        return "Suggestion Service is currently unavailable. Please try again later.";
    }

    public Trip getTripById(UUID tripId) {
        // Exemplo de como usar a chamada com circuit breaker
        logger.info("Checking Suggestion Service status...");
        String suggestionStatus = checkSuggestionServiceHealth();
        logger.info("Suggestion Service status: {}", suggestionStatus);

        return this.tripRepository.findById(tripId)
                .orElseThrow(() -> new TripNotFoundException("Trip not found."));
    }

    public List<TripGetResponse> getAllTrips() {
        return this.tripRepository.findAll().stream()
                .map(TripGetResponse::fromEntity)
                .toList();
    }


    public TripCreateResponse createTrip(TripCreateRequest trip, UUID userId) {
        Trip newTrip = Trip.builder()
                .userId(userId)
                .destination(trip.destination())
                .startsAt(trip.startsAt())
                .endsAt(trip.endsAt())
                .isConfirmed(false)
                .build();

        Trip savedTrip = this.tripRepository.save(newTrip);

        // Criar evento estruturado
        TripCreatedEvent event = new TripCreatedEvent(
                savedTrip.getId(),
                savedTrip.getUserId(),
                savedTrip.getDestination(),
                savedTrip.getStartsAt(),
                savedTrip.getEndsAt(),
                savedTrip.getCreatedAt()
        );

        System.out.println("Publicando evento RabbitMQ p/ trip com ID: " + savedTrip.getId());
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE_NAME,
                RabbitMQConfig.ROUTING_KEY,
                event // Enviando evento estruturado em JSON
        );


        return TripCreateResponse.fromEntity(savedTrip);
    }

    public Trip updateTrip(UUID tripId, TripUpdateRequest tripUpdateRequest, UUID userId) {
        Trip existingTrip = this.tripRepository.findById(tripId)
                .orElseThrow(() -> new TripNotFoundException("Trip not found."));

        // Validar se o userId da requisição é o mesmo do proprietário da viagem
        if (!existingTrip.getUserId().equals(userId)) {
            throw new RuntimeException("Unauthorized to update this trip."); // Ou uma exceção mais específica
        }

        existingTrip.setDestination(tripUpdateRequest.destination());
        existingTrip.setStartsAt(tripUpdateRequest.startsAt());
        existingTrip.setEndsAt(tripUpdateRequest.endsAt());
        if (tripUpdateRequest.isConfirmed() != null) {
            existingTrip.setConfirmed(tripUpdateRequest.isConfirmed());
        }

        return this.tripRepository.save(existingTrip);
    }

    public void deleteTrip(UUID tripId, UUID userId) {
        Trip existingTrip = this.tripRepository.findById(tripId)
                .orElseThrow(() -> new TripNotFoundException("Trip not found."));

        // Validar se o userId da requisição é o mesmo do proprietário da viagem
        if (!existingTrip.getUserId().equals(userId)) {
            throw new RuntimeException("Unauthorized to delete this trip."); // Ou uma exceção mais específica
        }

        this.tripRepository.delete(existingTrip);
    }
}
