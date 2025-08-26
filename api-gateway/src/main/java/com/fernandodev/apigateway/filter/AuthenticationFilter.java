package com.fernandodev.apigateway.filter;

import com.fernandodev.apigateway.ApiGatewayApplication;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import org.springframework.beans.factory.annotation.Value;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationFilter.class);

    // TODO: A chave secreta deve ser carregada de uma maneira mais segura (e.g., variável de ambiente, Vault)
    @Value("${jwt.secret}")
    private String secret;
    private SecretKey secretKey;

    @Value("${public-endpoints}")
    private List<String> publicEndpoints;

    public AuthenticationFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();

            // Permitir acesso a endpoints públicos (e.g., login, registro)
            if (isPublicEndpoint(request)) {
                return chain.filter(exchange);
            }

            // 1. Verificar se o cabeçalho Authorization está presente
            if (!request.getHeaders().containsKey("Authorization")) {
                return onError(exchange, "Authorization header missing", HttpStatus.UNAUTHORIZED);
            }

            String authHeader = request.getHeaders().getFirst("Authorization");
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return onError(exchange, "Invalid Authorization header", HttpStatus.UNAUTHORIZED);
            }

            String token = authHeader.substring(7);

            try {
                // Inicializa a chave secreta uma vez
                if (this.secretKey == null) {
                    this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
                }

                // 2. Validar o token JWT e extrair as claims
                Jws<Claims> claimsJws = Jwts.parserBuilder()
                        .setSigningKey(secretKey)
                        .build()
                        .parseClaimsJws(token);

                Claims claims = claimsJws.getBody();

                // 3. Adicionar o userId (e outras claims) aos cabeçalhos da requisição
                String userId = claims.get("userId", String.class);
                String userEmail = claims.get("sub", String.class); // 'sub' geralmente contém o email ou username

                if (userId == null || userEmail == null) {
                    return onError(exchange, "Token claims missing (userId or sub)", HttpStatus.UNAUTHORIZED);
                }

                ServerHttpRequest modifiedRequest = request.mutate()
                        .header("X-User-Id", userId)
                        .header("X-User-Email", userEmail)
                        .build();

                return chain.filter(exchange.mutate().request(modifiedRequest).build());

            } catch (Exception e) {
                logger.error("JWT Validation Error: {}", e.getMessage());
                return onError(exchange, "Invalid or expired JWT token", HttpStatus.UNAUTHORIZED);
            }
        };
    }

    private Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus httpStatus) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);
        response.getHeaders().add("Content-Type", "application/json");

        String errorResponse = String.format("{\"status\":%d, \"error\":\"%s\"}", httpStatus.value(), err);

        return response.writeWith(Mono.just(response.bufferFactory().wrap(errorResponse.getBytes(StandardCharsets.UTF_8))));
    }

    private boolean isPublicEndpoint(ServerHttpRequest request) {
        String path = request.getURI().getPath();
        // TODO: Definir endpoints públicos de forma mais robusta (e.g., arquivo de configuração)
        return publicEndpoints.stream().anyMatch(path::startsWith);
    }

    public static class Config {
        // Não são necessárias configurações específicas para este filtro por enquanto
    }
}
