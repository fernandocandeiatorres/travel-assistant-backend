package com.fernandodev.suggestionservice.controller;

import com.fernandodev.suggestionservice.model.Suggestion;
import com.fernandodev.suggestionservice.service.SuggestionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/suggestions")
@RequiredArgsConstructor
@Slf4j
public class SuggestionController {

    private final SuggestionService suggestionService;
    private final RedisTemplate<String, Object> redisTemplate;

    @GetMapping("/trip/{tripId}")
    public ResponseEntity<List<Suggestion>> getSuggestionsByTripId(@PathVariable UUID tripId) {
        
        // Verificar se existe no cache antes de chamar o service
        String cacheKey = "suggestions::" + tripId;
        boolean existsInCache = Boolean.TRUE.equals(redisTemplate.hasKey(cacheKey));
        
        if (existsInCache) {
            log.info("⚡ REDIS CACHE HIT - Retornando do cache para tripId: {}", tripId);
        } else {
            log.info("🔍 CACHE MISS - Será buscado no banco para tripId: {}", tripId);
        }
        
        List<Suggestion> suggestions = suggestionService.getSuggestionsByTripId(tripId);
        
        log.info("📊 Retornando {} suggestions para tripId: {}", suggestions.size(), tripId);
        
        return ResponseEntity.ok(suggestions);
    }
    
    @GetMapping("/{suggestionId}")
    public ResponseEntity<Suggestion> getSuggestionById(@PathVariable UUID suggestionId) {
        Suggestion suggestion = suggestionService.getSuggestionByTripId(suggestionId);
        return ResponseEntity.ok(suggestion);
    }
    
    // Endpoint para debug do cache
    @GetMapping("/cache/debug/{tripId}")
    public ResponseEntity<String> debugCache(@PathVariable UUID tripId) {
        String cacheKey = "suggestions::" + tripId;
        boolean exists = Boolean.TRUE.equals(redisTemplate.hasKey(cacheKey));
        
        if (exists) {
            Object cached = redisTemplate.opsForValue().get(cacheKey);
            return ResponseEntity.ok("✅ Cache EXISTS para " + tripId + ". Tipo: " + 
                (cached != null ? cached.getClass().getSimpleName() : "null"));
        } else {
            return ResponseEntity.ok("❌ Cache NÃO EXISTE para " + tripId);
        }
    }
}
