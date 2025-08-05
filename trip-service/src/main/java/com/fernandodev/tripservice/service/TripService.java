package com.fernandodev.tripservice.service;


import com.fernandodev.tripservice.config.RabbitMQConfig;
import com.fernandodev.tripservice.dto.TripCreateRequest;
import com.fernandodev.tripservice.dto.TripCreateResponse;
import com.fernandodev.tripservice.dto.TripCreatedEvent;
import com.fernandodev.tripservice.exception.TripNotFoundException;
import com.fernandodev.tripservice.model.Trip;
import com.fernandodev.tripservice.repository.TripRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class TripService {

    private final TripRepository tripRepository;
    private final RabbitTemplate rabbitTemplate;

    public Trip getTripById(UUID tripId) {
        return this.tripRepository.findById(tripId)
                .orElseThrow(() -> new TripNotFoundException("Trip not found."));
    }

    public List<Trip> getAllTrips() {
        return this.tripRepository.findAll();
    }


    public TripCreateResponse createTrip(TripCreateRequest trip) {
        Trip newTrip = Trip.builder()
                .userId(UUID.randomUUID()) // TODO: Obter do contexto de autenticação
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
}
