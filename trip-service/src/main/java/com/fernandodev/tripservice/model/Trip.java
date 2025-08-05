package com.fernandodev.tripservice.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "trips")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Trip {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private UUID userId;

    private String destination;

    private LocalDate startsAt;

    private LocalDate endsAt;

    private boolean isConfirmed;

    @CreationTimestamp
    private LocalDateTime createdAt;


}
