package com.fernandodev.apigateway;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(properties = {
    "jwt.secret=test-secret-key-for-context-load-test-12345678901234567890",
    "public-endpoints=/api/v1/users/register,/api/v1/users/login"
})
class ApiGatewayApplicationTests {

    @Test
    void contextLoads() {
    }

}
