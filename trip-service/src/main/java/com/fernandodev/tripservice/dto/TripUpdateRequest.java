package com.fernandodev.tripservice.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

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
