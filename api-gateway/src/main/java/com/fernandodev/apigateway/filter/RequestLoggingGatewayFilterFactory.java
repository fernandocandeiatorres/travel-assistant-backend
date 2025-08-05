package com.fernandodev.apigateway.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Component
@Slf4j
public class RequestLoggingGatewayFilterFactory extends AbstractGatewayFilterFactory<RequestLoggingGatewayFilterFactory.Config> {

    public RequestLoggingGatewayFilterFactory() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            // Log da requisição ANTES de ser enviada para o serviço
            var request = exchange.getRequest();

            log.info("🚀 GATEWAY REQUEST: {} {} - Headers: {} - Timestamp: {}",
                    request.getMethod(),
                    request.getURI(),
                    request.getHeaders().keySet(),
                    LocalDateTime.now()
            );

            // Continuar com a requisição e log da resposta
            return chain.filter(exchange).then(
                    Mono.fromRunnable(() -> {
                        var response = exchange.getResponse();
                        log.info("✅ GATEWAY RESPONSE: Status {} - Headers: {} - Timestamp: {}",
                                response.getStatusCode(),
                                response.getHeaders().keySet(),
                                LocalDateTime.now()
                        );
                    })
            );
        };
    }

    // Classe de configuração (pode ser vazia para filtros simples)
    public static class Config {
        // Configurações do filtro (se necessário)
    }
}