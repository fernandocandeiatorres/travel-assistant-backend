package com.fernandodev.tripservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fernandodev.tripservice.dto.TripCreateRequest;
import com.fernandodev.tripservice.dto.TripCreateResponse;
import com.fernandodev.tripservice.dto.TripGetResponse;
import com.fernandodev.tripservice.dto.TripUpdateRequest;
import com.fernandodev.tripservice.model.Trip;
import com.fernandodev.tripservice.service.TripService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;
import com.fernandodev.tripservice.environment.InstanceInformationService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

@SpringBootTest
@AutoConfigureMockMvc
public class TripControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TripService tripService;

    @MockBean
    private RestTemplate restTemplate;

    @MockBean
    private InstanceInformationService instanceInformationService;

    @MockBean
    private RabbitTemplate rabbitTemplate;

    private static UUID userMockID;

    @BeforeAll
    public static void setUp() {
        // Any global setup can be done here
        userMockID = UUID.randomUUID();
    }

    @Test
    public void shouldCreateTrip() throws Exception {
        TripCreateRequest requestDto = new TripCreateRequest("Paris", LocalDate.now(), LocalDate.now().plusDays(5));
        TripCreateResponse responseDto = new TripCreateResponse(UUID.randomUUID(), userMockID, "Paris", LocalDate.now(), LocalDate.now().plusDays(5), null);

        when(tripService.createTrip(any(TripCreateRequest.class), any(UUID.class))).thenReturn(responseDto);

        mockMvc.perform(post("/api/v1/trips/create")
                        .header("X-User-Id", userMockID.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(responseDto.id().toString()))
                .andExpect(jsonPath("$.userId").value(userMockID.toString()));
    }

    @Test
    public void shouldGetTrip() throws Exception {

        Trip responseTrip = Trip.builder()
                .id(userMockID)
                .userId(userMockID)
                .startsAt(LocalDate.now())
                .endsAt(LocalDate.now().plusDays(5))
                .destination("Paris")
                .isConfirmed(true)
                .build();

        when(tripService.getTripById(userMockID)).thenReturn(responseTrip);

        mockMvc.perform(get("/api/v1/trips/{id}", userMockID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userMockID.toString()))
                .andExpect(jsonPath("$.destination").value("Paris"));
    }

    @Test
    public void shouldGetAllTrips() throws Exception {
        TripGetResponse trip1 = TripGetResponse.builder()
                .id(UUID.randomUUID())
                .userId(userMockID)
                .destination("Rome")
                .startsAt(LocalDate.now())
                .endsAt(LocalDate.now().plusDays(7))
                .isConfirmed(false)
                .build();
        TripGetResponse trip2 = TripGetResponse.builder()
                .id(UUID.randomUUID())
                .userId(userMockID)
                .destination("Berlin")
                .startsAt(LocalDate.now().plusDays(10))
                .endsAt(LocalDate.now().plusDays(15))
                .isConfirmed(true)
                .build();
        List<TripGetResponse> allTrips = Arrays.asList(trip1, trip2);

        when(tripService.getAllTrips()).thenReturn(allTrips);

        mockMvc.perform(get("/api/v1/trips"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].destination").value("Rome"))
                .andExpect(jsonPath("$[1].destination").value("Berlin"));
    }

    @Test
    public void shouldUpdateTrip() throws Exception {
        UUID tripId = UUID.randomUUID();
        TripUpdateRequest requestDto = new TripUpdateRequest("London", LocalDate.now().plusDays(1), LocalDate.now().plusDays(7), true);

        Trip updatedTrip = Trip.builder()
                .id(tripId)
                .userId(userMockID)
                .destination(requestDto.destination())
                .startsAt(requestDto.startsAt())
                .endsAt(requestDto.endsAt())
                .isConfirmed(requestDto.isConfirmed())
                .build();

        when(tripService.updateTrip(eq(tripId), any(TripUpdateRequest.class), any(UUID.class))).thenReturn(updatedTrip);

        mockMvc.perform(put("/api/v1/trips/{tripId}", tripId)
                        .header("X-User-Id", userMockID.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(tripId.toString()))
                .andExpect(jsonPath("$.destination").value("London"))
                .andExpect(jsonPath("$.isConfirmed").value(true));
    }

    @Test
    public void shouldDeleteTrip() throws Exception {
        UUID tripId = UUID.randomUUID();

        mockMvc.perform(delete("/api/v1/trips/{tripId}", tripId)
                        .header("X-User-Id", userMockID.toString()))
                .andExpect(status().isNoContent());

        verify(tripService, times(1)).deleteTrip(eq(tripId), eq(userMockID));
    }
}
