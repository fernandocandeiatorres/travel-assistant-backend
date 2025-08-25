package com.fernandodev.suggestionservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fernandodev.suggestionservice.dto.SuggestionCreateDTO;
import com.fernandodev.suggestionservice.model.BudgetType;
import com.fernandodev.suggestionservice.model.Suggestion;
import com.fernandodev.suggestionservice.service.SuggestionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class SuggestionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private SuggestionService suggestionService;

    @Test
    public void shouldGetSuggestionsByTripId() throws Exception {
        UUID tripId = UUID.randomUUID();
        when(suggestionService.getSuggestionsByTripId(any(UUID.class))).thenReturn(Collections.singletonList(new Suggestion()));

        mockMvc.perform(get("/api/v1/suggestions/trip/" + tripId))
                .andExpect(status().isOk());
    }
}
