package com.fernandodev.tripservice.dto;

import com.fernandodev.tripservice.model.Trip;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record TripCreateResponse(UUID id, UUID userId, String destination, LocalDate startsAt, LocalDate endsAt, LocalDateTime createdAt) {
    public static TripCreateResponse fromEntity(Trip trip) {
        return new TripCreateResponse(trip.getId(), trip.getUserId(), trip.getDestination(), trip.getStartsAt(), trip.getEndsAt(), trip.getCreatedAt());
    }
}
