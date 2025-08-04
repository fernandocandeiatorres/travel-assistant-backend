package com.fernandodev.tripservice.dto;

import com.fernandodev.tripservice.model.Trip;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record TripGetResponse(
        UUID id,
        UUID userId,
        String destination,
        LocalDate startsAt,
        LocalDate endsAt,
        boolean isConfirmed

) {
    public static TripGetResponse fromEntity(Trip trip) {
        return new TripGetResponse(
                trip.getId(),
                trip.getUserId(),
                trip.getDestination(),
                trip.getStartsAt(),
                trip.getEndsAt(),
                trip.isConfirmed()
        );
    }
}
