package com.fernandodev.suggestionservice.repository;

import com.fernandodev.suggestionservice.model.Suggestion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SuggestionRepository extends JpaRepository<Suggestion, UUID> {
    Optional<Suggestion> findByTripId(UUID tripId);
    List<Suggestion> findAllByTripId(UUID tripId);
}
