package com.fernandodev.suggestionservice.dto;

import com.fernandodev.suggestionservice.model.Itinerary;
import com.fernandodev.suggestionservice.model.Suggestion;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public record SuggestionCreateDTO(
        UUID id,
        UUID tripId,
        String destination,
        List<Itinerary> itineraries,
        LocalDateTime createdAt
) {
    public static SuggestionCreateDTO fromEntity(Suggestion suggestion) {
        return new SuggestionCreateDTO(
                suggestion.getId(),
                suggestion.getTripId(),
                suggestion.getDestination(),
                suggestion.getItineraries(),
                suggestion.getCreatedAt()
        );
    }
}
