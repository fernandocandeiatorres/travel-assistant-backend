package com.fernandodev.suggestionservice.controller;

import com.fernandodev.suggestionservice.service.SuggestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController("/api/v1/suggestions")
@RequiredArgsConstructor
public class SuggestionController {

    private final SuggestionService suggestionService;

    public ResponseEntity<?> getSuggestionByTripId(UUID tripId) {
        return new ResponseEntity<>(suggestionService.getSuggestionByTripId(tripId), HttpStatus.OK);
    }
}
