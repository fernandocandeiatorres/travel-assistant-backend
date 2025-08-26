package com.fernandodev.apigateway.filter;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import java.util.UUID;

@Component
public class CorrelationIdFilter implements GlobalFilter, Ordered {

    public static final String CORRELATION_ID_HEADER = "X-Correlation-ID";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String correlationId = getCorrelationId(request);

        // Adiciona o ID de correlação ao cabeçalho da requisição
        ServerHttpRequest modifiedRequest = request.mutate()
                .header(CORRELATION_ID_HEADER, correlationId)
                .build();

        // Adiciona o ID de correlação ao cabeçalho da resposta
        exchange.getResponse().getHeaders().add(CORRELATION_ID_HEADER, correlationId);

        return chain.filter(exchange.mutate().request(modifiedRequest).build());
    }

    private String getCorrelationId(ServerHttpRequest request) {
        if (request.getHeaders().containsKey(CORRELATION_ID_HEADER)) {
            return request.getHeaders().getFirst(CORRELATION_ID_HEADER);
        } else {
            return UUID.randomUUID().toString();
        }
    }

    @Override
    public int getOrder() {
        // Executa este filtro antes dos outros
        return Ordered.HIGHEST_PRECEDENCE;
    }
}
