package com.fernandodev.tripservice.service;


import com.fernandodev.tripservice.config.RabbitMQConfig;
import com.fernandodev.tripservice.dto.TripCreateRequest;
import com.fernandodev.tripservice.dto.TripCreateResponse;
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
                .destination(trip.destination())
                .startsAt(trip.startsAt())
                .endsAt(trip.endsAt())
                .isConfirmed(false)
                .build();

        Trip savedTrip = this.tripRepository.save(newTrip);

        System.out.println("Publicando evento RabbitMQ p/ trip com ID: " + savedTrip.getId());
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE_NAME,
                RabbitMQConfig.ROUTING_KEY,
                newTrip.getId() // Unica coisa que o suggestion-service precisa para trabalhar
        );


        return TripCreateResponse.fromEntity(savedTrip);
    }
}
