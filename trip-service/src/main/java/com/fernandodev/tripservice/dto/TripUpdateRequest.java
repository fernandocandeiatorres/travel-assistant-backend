package com.fernandodev.tripservice.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record TripCreateResponse(UUID id, UUID userId, String destination, LocalDate startsAt, LocalDate endsAt, LocalDateTime createdAt) {
    public static TripCreateResponse fromEntity(com.fernandodev.tripservice.model.Trip trip) {
        return new TripCreateResponse(trip.getId(), trip.getUserId(), trip.getDestination(), trip.getStartsAt(), trip.getEndsAt(), trip.getCreatedAt());
    }
}

// Novo DTO para atualização de viagem
public record TripUpdateRequest(
        @NotNull(message = "Destination cannot be null")
        String destination,
        @NotNull(message = "Start date cannot be null")
        LocalDate startsAt,
        @NotNull(message = "End date cannot be null")
        LocalDate endsAt,
        Boolean isConfirmed
) {
}
