package com.fernandodev.apigateway.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.test.web.reactive.server.WebTestClient;

@WebFluxTest(GatewayController.class)
class GatewayControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void healthShouldReturnOk() {
        webTestClient.get().uri("/api/gateway/health")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.status").isEqualTo("UP")
                .jsonPath("$.service").isEqualTo("API Gateway");
    }

    @Test
    void infoShouldReturnOk() {
        webTestClient.get().uri("/api/gateway/info")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.application").isEqualTo("Travel Assistant API Gateway")
                .jsonPath("$.version").isEqualTo("1.0.0");
    }

    @Test
    void statusShouldReturnOk() {
        webTestClient.get().uri("/api/gateway/status")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.gateway_status").isEqualTo("ACTIVE")
                .jsonPath("$.total_routes").isEqualTo(2);
    }
}
