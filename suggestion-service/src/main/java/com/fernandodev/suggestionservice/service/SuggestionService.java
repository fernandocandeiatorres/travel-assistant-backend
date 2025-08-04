package com.fernandodev.suggestionservice.service;


import com.fernandodev.suggestionservice.dto.SuggestionCreateDTO;
import com.fernandodev.suggestionservice.model.BudgetType;
import com.fernandodev.suggestionservice.model.Itinerary;
import com.fernandodev.suggestionservice.model.Suggestion;
import com.fernandodev.suggestionservice.repository.SuggestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SuggestionService {

    private final SuggestionRepository suggestionRepository;

    public SuggestionCreateDTO generateSuggestionFor(UUID tripId) {
        Suggestion newSuggestion = Suggestion.builder()
                .createdAt(LocalDateTime.now())
                .tripId(tripId)
                .build();

        Itinerary newItinerary = Itinerary.builder()
                .suggestion(newSuggestion)
                .budgetType(BudgetType.ECONOMIC)
                .activityDetails(generateItinerariesMock())
                .build();

        Itinerary newItinerary2 = Itinerary.builder()
                .suggestion(newSuggestion)
                .budgetType(BudgetType.MEDIUM)
                .activityDetails(generateItinerariesMock())
                .build();

        Itinerary newItinerary3 = Itinerary.builder()
                .suggestion(newSuggestion)
                .budgetType(BudgetType.LUXURY)
                .activityDetails(generateItinerariesMock())
                .build();

        newSuggestion.addItinerary(newItinerary);
        newSuggestion.addItinerary(newItinerary2);
        newSuggestion.addItinerary(newItinerary3);

        suggestionRepository.save(newSuggestion);

        SuggestionCreateDTO response = SuggestionCreateDTO.fromEntity(newSuggestion);
        return response;
    }

    public Suggestion getSuggestionByTripId(UUID tripId) {
        return suggestionRepository.findByTripId(tripId)
                .orElseThrow(
                        () -> new RuntimeException("No suggestions found for trip Id.")
                );
    }


    private String generateItinerariesMock() {
        return "mock LLM DATA TEST";
    }
}
