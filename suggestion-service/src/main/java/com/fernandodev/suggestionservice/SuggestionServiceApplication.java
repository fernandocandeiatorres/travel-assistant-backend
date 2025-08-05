package com.fernandodev.suggestionservice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class SuggestionServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(SuggestionServiceApplication.class, args);
    }

}
