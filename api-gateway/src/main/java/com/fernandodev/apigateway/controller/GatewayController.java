package com.fernandodev.apigateway.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/gateway")
public class GatewayController {

    @GetMapping("/health")
    public Mono<Map<String, Object>> health() {
        return Mono.just(Map.of(
                "status", "UP",
                "service", "API Gateway",
                "timestamp", LocalDateTime.now().toString(),
                "description", "Gateway is running and routing traffic"
        ));
    }

    @GetMapping("/info")
    public Mono<Map<String, Object>> info() {
        return Mono.just(Map.of(
                "application", "Travel Assistant API Gateway",
                "version", "1.0.0",
                "description", "Centralized entry point for all microservices",
                "author", "Fernando Torres",
                "configured_routes", List.of(
                        Map.of(
                                "id", "trip-service-route",
                                "path", "/api/v1/trips/**",
                                "target", "http://localhost:8000"
                        ),
                        Map.of(
                                "id", "suggestion-service-route",
                                "path", "/api/v1/suggestions/**",
                                "target", "http://localhost:8010"
                        )
                )
        ));
    }

    @GetMapping("/status")
    public Mono<Map<String, Object>> status() {
        return Mono.just(Map.of(
                "total_routes", 2,
                "gateway_status", "ACTIVE",
                "services", Map.of(
                        "trip-service", "localhost:8000",
                        "suggestion-service", "localhost:8010"
                ),
                "last_check", LocalDateTime.now().toString()
        ));
    }
}