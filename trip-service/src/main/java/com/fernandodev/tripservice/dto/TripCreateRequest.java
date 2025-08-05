package com.fernandodev.tripservice.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record TripCreateRequest(
        @NotBlank(message = "Destination can't be empty")
        String destination,

        @NotNull(message = "Start date can't be empty.")
        @Future(message = "Date must be in the future.")
        @JsonFormat(pattern = "dd/MM/yyyy")
        @JsonProperty("starts_at")
        LocalDate startsAt,

        @NotNull(message = "End date can't be empty.")
        @Future(message = "Date must be in the future.")
        @JsonFormat(pattern = "dd/MM/yyyy")
        @JsonProperty("ends_at")
        LocalDate endsAt
) {

}


