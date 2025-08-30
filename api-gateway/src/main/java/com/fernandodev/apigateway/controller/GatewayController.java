package com.fernandodev.apigateway.controller;

import org.springframework.cloud.client.discovery.ReactiveDiscoveryClient;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/gateway")
public class GatewayController {

    private final RouteLocator routeLocator;
    private final ReactiveDiscoveryClient discoveryClient;

    public GatewayController(RouteLocator routeLocator, ReactiveDiscoveryClient discoveryClient) {
        this.routeLocator = routeLocator;
        this.discoveryClient = discoveryClient;
    }

    // Health check simples
    @GetMapping("/health")
    public Mono<Map<String, Object>> health() {
        return Mono.just(Map.of(
                "status", "UP",
                "service", "API Gateway",
                "timestamp", LocalDateTime.now().toString(),
                "description", "Gateway is running and routing traffic"
        ));
    }

    // Informações básicas do Gateway
    @GetMapping("/info")
    public Mono<Map<String, Object>> info() {
        return Mono.just(Map.of(
                "application", "Travel Assistant API Gateway",
                "version", "1.0.0",
                "description", "Centralized entry point for all microservices",
                "author", "Fernando Torres"
        ));
    }

    // Lista as rotas reais do Spring Cloud Gateway
    @GetMapping("/routes")
    public Flux<Map<String, Object>> routes() {
        return routeLocator.getRoutes().map(route -> Map.of(
                "id", route.getId(),
                "uri", route.getUri().toString(),
                "predicate", route.getPredicate().toString()
        ));
    }

    // Lista os serviços registrados no Consul
    @GetMapping("/services")
    public Flux<Map<String, Object>> services() {
        return discoveryClient.getServices()
                .flatMap(service -> discoveryClient.getInstances(service)
                        .map(instance -> Map.of(
                                "serviceId", service,
                                "host", instance.getHost(),
                                "port", instance.getPort(),
                                "uri", instance.getUri().toString()
                        ))
                );
    }

    // Status geral do gateway (resumo)
    @GetMapping("/status")
    public Mono<Map<String, Object>> status() {
        Mono<List<String>> services = discoveryClient.getServices().collectList();
        Mono<List<Map<String, Object>>> routes = routeLocator.getRoutes()
                .map(route -> Map.<String, Object>of(
                        "id", route.getId(),
                        "uri", route.getUri().toString()
                ))
                .collectList();

        return Mono.zip(services, routes)
                .map(tuple -> Map.of(
                        "gateway_status", "ACTIVE",
                        "total_services", tuple.getT1().size(),
                        "registered_services", tuple.getT1(),
                        "total_routes", tuple.getT2().size(),
                        "configured_routes", tuple.getT2(),
                        "last_check", LocalDateTime.now().toString()
                ));
    }
}
