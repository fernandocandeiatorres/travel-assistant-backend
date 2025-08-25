package com.fernandodev.tripservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fernandodev.tripservice.dto.TripCreateRequest;
import com.fernandodev.tripservice.dto.TripCreateResponse;
import com.fernandodev.tripservice.dto.TripGetResponse;
import com.fernandodev.tripservice.service.TripService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class TripControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TripService tripService;

    @Test
    public void shouldCreateTrip() throws Exception {
        TripCreateRequest requestDto = new TripCreateRequest("Paris", LocalDate.now(), LocalDate.now().plusDays(5));
        TripCreateResponse responseDto = new TripCreateResponse(1L);

        when(tripService.createTrip(any(TripCreateRequest.class))).thenReturn(responseDto);

        mockMvc.perform(post("/api/v1/trips/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.tripId").value(1L));
    }

    @Test
    public void shouldGetTrip() throws Exception {
        TripGetResponse responseDto = new TripGetResponse(1L, "Paris", LocalDate.now(), LocalDate.now().plusDays(5));

        when(tripService.getTripById(anyLong())).thenReturn(responseDto);

        mockMvc.perform(get("/api/v1/trips/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.destination").value("Paris"));
    }
}
