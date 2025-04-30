package ru.otus.hw.config;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class ResilienceConfig {
    public static final String DB_CIRCUIT_BREAKER = "dbCircuitBreaker";

    @Bean
    public CircuitBreakerConfig circuitBreakerConfig() {
        return CircuitBreakerConfig
                .custom()
                .slowCallRateThreshold(5)
                .slowCallDurationThreshold(Duration.ofMillis(500))
                .minimumNumberOfCalls(16)
                .build();
    }

    @Bean
    public CircuitBreakerRegistry circuitBreakerRegistry() {
        return CircuitBreakerRegistry.of(circuitBreakerConfig());
    }

    @Bean
    public CircuitBreaker circuitBreaker() {
        return circuitBreakerRegistry().circuitBreaker(DB_CIRCUIT_BREAKER);
    }
}
