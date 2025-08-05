package com.fernandodev.suggestionservice.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name="itineraries")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Itinerary {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BudgetType budgetType;

    @Column(columnDefinition = "TEXT")
    private String loggingDetails;

    @Column(columnDefinition = "TEXT")
    private String restaurantDetails;

    @Column(columnDefinition = "TEXT")
    private String activityDetails;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="suggestion_id", nullable = false)
    @JsonBackReference
    private Suggestion suggestion;

}
