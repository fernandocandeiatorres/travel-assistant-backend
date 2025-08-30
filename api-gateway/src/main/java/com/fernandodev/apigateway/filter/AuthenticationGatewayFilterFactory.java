package com.fernandodev.apigateway.filter;

import com.fernandodev.apigateway.config.GatewayConfigProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

@Component
@Slf4j
public class AuthenticationGatewayFilterFactory extends AbstractGatewayFilterFactory<Object> {

    @Autowired
    private GatewayConfigProperties configProperties;

    @Override
    public GatewayFilter apply(Object config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            String path = request.getPath().toString();

            if (isPublicEndpoint(path)) {
                return chain.filter(exchange);
            }

            if (!request.getHeaders().containsKey("Authorization")) {
                return onError(exchange, HttpStatus.UNAUTHORIZED);
            }

            String authHeader = request.getHeaders().getFirst("Authorization");
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return onError(exchange, HttpStatus.UNAUTHORIZED);
            }

            String token = authHeader.substring(7);

            if (!validateToken(token)) {
                return onError(exchange, HttpStatus.UNAUTHORIZED);
            }

            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(Keys.hmacShaKeyFor(configProperties.getJwt().getSecret().getBytes()))
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            String userId = claims.get("userId", String.class);
            String userEmail = claims.get("sub", String.class);

            if (userId == null || userEmail == null) {
                return onError(exchange, HttpStatus.UNAUTHORIZED);
            }

            ServerHttpRequest modifiedRequest = request.mutate()
                    .header("X-User-Id", userId)
                    .header("X-User-Email", userEmail)
                    .build();

            return chain.filter(exchange.mutate().request(modifiedRequest).build());
        };
    }

    private Mono<Void> onError(ServerWebExchange exchange, HttpStatus status) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(status);
        response.getHeaders().add("Content-Type", "application/json");

        String errorResponse = String.format("{\"status\":%d, \"error\":\"%s\"}", status.value(), status.getReasonPhrase());

        return exchange.getResponse().writeWith(Mono.just(response.bufferFactory().wrap(errorResponse.getBytes(StandardCharsets.UTF_8))));
    }

    private boolean isPublicEndpoint(String path) {
        return configProperties.getPublicEndpoints().stream()
                .anyMatch(path::contains);
    }
    

    private boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(Keys.hmacShaKeyFor(configProperties.getJwt().getSecret().getBytes()))
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            log.error("JWT Validation Error: {}", e.getMessage());
            return false;
        }
    }
}
