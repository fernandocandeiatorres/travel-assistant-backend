package com.fernandodev.suggestionservice.model;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "suggestions")
public class Suggestion {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private UUID tripId;

    private String destination;

    @OneToMany(mappedBy = "suggestion", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    @Builder.Default
    private List<Itinerary> itineraries = new ArrayList<>();

    @CreationTimestamp
    private LocalDateTime createdAt;

    public void addItinerary(Itinerary itinerary) {
        this.itineraries.add(itinerary);
        itinerary.setSuggestion(this);
    }
}
