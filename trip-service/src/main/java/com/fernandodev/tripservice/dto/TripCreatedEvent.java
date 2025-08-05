package com.fernandodev.tripservice.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record TripCreatedEvent(
        UUID tripId,
        UUID userId,
        String destination,
        LocalDate startsAt,
        LocalDate endsAt,
        LocalDateTime createdAt
) {
}
