package com.fernandodev.tripservice.controller;

import com.fernandodev.tripservice.dto.TripCreateRequest;
import com.fernandodev.tripservice.dto.TripGetResponse;
import com.fernandodev.tripservice.dto.TripUpdateRequest;
import com.fernandodev.tripservice.environment.InstanceInformationService;
import com.fernandodev.tripservice.model.Trip;
import com.fernandodev.tripservice.service.TripService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController()
@RequestMapping("/api/v1/trips")
@RequiredArgsConstructor
public class TripController {

    private final TripService tripService;

    private final InstanceInformationService informationService;

    // localhost:8000/trip-service/<TripId>
    @GetMapping("/{tripId}")
    public ResponseEntity<?> getTripById(@PathVariable UUID tripId) {
//            String userEmail = auth
        Trip trip = tripService.getTripById(tripId);

        TripGetResponse response = TripGetResponse.fromEntity(trip);
        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    @GetMapping("")
    public ResponseEntity<List<TripGetResponse>> getAllTrips() {
        return new ResponseEntity<>(tripService.getAllTrips(), HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<?> createTrip(@RequestBody TripCreateRequest trip, @RequestHeader("X-User-Id") UUID userId) {
        return new ResponseEntity<>(tripService.createTrip(trip, userId), HttpStatus.CREATED);
    }

    @PutMapping("/{tripId}")
    public ResponseEntity<?> updateTrip(@PathVariable UUID tripId, @RequestBody TripUpdateRequest tripUpdateRequest, @RequestHeader("X-User-Id") UUID userId) {
        Trip updatedTrip = tripService.updateTrip(tripId, tripUpdateRequest, userId);
        TripGetResponse response = TripGetResponse.fromEntity(updatedTrip);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{tripId}")
    public ResponseEntity<?> deleteTrip(@PathVariable UUID tripId, @RequestHeader("X-User-Id") UUID userId) {
        tripService.deleteTrip(tripId, userId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
