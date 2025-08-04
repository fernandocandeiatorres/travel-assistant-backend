package com.fernandodev.tripservice.dto;

import com.fernandodev.tripservice.model.Trip;

import java.util.UUID;

public record TripCreateResponse(UUID tripId) {
    public static TripCreateResponse fromEntity(Trip trip) {
        return new TripCreateResponse(trip.getId());
    }
}
