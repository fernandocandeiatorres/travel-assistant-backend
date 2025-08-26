package com.fernandodev.suggestionservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fernandodev.suggestionservice.model.Suggestion;
import com.fernandodev.suggestionservice.service.SuggestionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SuggestionController.class)
public class SuggestionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private SuggestionService suggestionService;

    @MockBean
    private RedisTemplate<String, Object> redisTemplate; // Mock para evitar a dependência do Redis

    @Test
    void shouldReturnOkForHealthCheck() throws Exception {
        String expectedResponse = "Suggestion Service is UP";
        when(suggestionService.getHealthStatus()).thenReturn(expectedResponse);

        mockMvc.perform(get("/api/v1/suggestions/health"))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedResponse));
    }

    @Test
    void shouldGetSuggestionsByTripId() throws Exception {
        UUID tripId = UUID.randomUUID();
        Suggestion suggestion = new Suggestion();
        suggestion.setId(UUID.randomUUID());
        suggestion.setTripId(tripId);

        when(suggestionService.getSuggestionsByTripId(any(UUID.class))).thenReturn(Collections.singletonList(suggestion));
        // Simula que a chave não existe no cache para forçar a chamada ao serviço
        when(redisTemplate.hasKey(any(String.class))).thenReturn(false);

        mockMvc.perform(get("/api/v1/suggestions/trip/{tripId}", tripId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].tripId").value(tripId.toString()));
    }

    @Test
    void shouldGetSuggestionById() throws Exception {
        UUID suggestionId = UUID.randomUUID();
        Suggestion suggestion = new Suggestion();
        suggestion.setId(suggestionId);

        when(suggestionService.getSuggestionById(any(UUID.class))).thenReturn(suggestion);

        mockMvc.perform(get("/api/v1/suggestions/{suggestionId}", suggestionId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(suggestionId.toString()));
    }
}
