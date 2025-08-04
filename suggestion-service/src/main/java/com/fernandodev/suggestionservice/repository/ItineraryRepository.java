package com.fernandodev.suggestionservice.repository;

import com.fernandodev.suggestionservice.model.Itinerary;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ItineraryRepository extends JpaRepository<Itinerary, UUID> {
}
