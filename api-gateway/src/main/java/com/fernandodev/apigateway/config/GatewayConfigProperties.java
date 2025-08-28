package com.fernandodev.apigateway.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ConfigurationProperties
@Data
public class GatewayConfigProperties {
    private Jwt jwt;
    private List<String> publicEndpoints;

    @Data
    public static class Jwt {
        private String secret;
    }
}
