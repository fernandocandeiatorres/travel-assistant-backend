package com.fernandodev.suggestionservice.service;


import com.fernandodev.suggestionservice.dto.SuggestionCreateDTO;
import com.fernandodev.suggestionservice.dto.TripCreatedEvent;
import com.fernandodev.suggestionservice.model.BudgetType;
import com.fernandodev.suggestionservice.model.Itinerary;
import com.fernandodev.suggestionservice.model.Suggestion;
import com.fernandodev.suggestionservice.repository.SuggestionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class SuggestionService {

    private final SuggestionRepository suggestionRepository;

    public SuggestionCreateDTO generateSuggestionFor(TripCreatedEvent event) {
        Suggestion newSuggestion = Suggestion.builder()
                .createdAt(LocalDateTime.now())
                .tripId(event.tripId())
                .destination(event.destination()) // Usando dados do evento
                .build();

        Itinerary newItinerary = Itinerary.builder()
                .suggestion(newSuggestion)
                .budgetType(BudgetType.ECONOMIC)
                .activityDetails(generateItinerariesMock(event.destination(), BudgetType.ECONOMIC))
                .build();

        Itinerary newItinerary2 = Itinerary.builder()
                .suggestion(newSuggestion)
                .budgetType(BudgetType.MEDIUM)
                .activityDetails(generateItinerariesMock(event.destination(), BudgetType.MEDIUM))
                .build();

        Itinerary newItinerary3 = Itinerary.builder()
                .suggestion(newSuggestion)
                .budgetType(BudgetType.LUXURY)
                .activityDetails(generateItinerariesMock(event.destination(), BudgetType.LUXURY))
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

    @Cacheable(value = "suggestions", key = "#tripId")
    public List<Suggestion> getSuggestionsByTripId(UUID tripId) {
        List<Suggestion> suggestions = suggestionRepository.findAllByTripId(tripId);
        return suggestions;
    }


    private String generateItinerariesMock(String destination, BudgetType budgetType) {
        return String.format("Sugestões %s para %s: Mock LLM Data para orçamento %s", 
                budgetType.name(), destination, budgetType.name());
    }
}
