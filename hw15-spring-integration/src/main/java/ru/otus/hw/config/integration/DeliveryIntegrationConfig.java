package ru.otus.hw.config.integration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.MessageChannelSpec;
import org.springframework.integration.dsl.MessageChannels;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
public class DeliveryIntegrationConfig {
    private static final int POOL_SIZE = 4;

    @Bean
    public ExecutorService postmanPool() {
        return Executors.newFixedThreadPool(POOL_SIZE);
    }

    @Bean
    public MessageChannelSpec<?, ?> postmanChannel() {
        return MessageChannels.executor(postmanPool());
    }

    @Bean
    public IntegrationFlow deliveryIntegrationFlow() {
        return IntegrationFlow.from("deliveryInputChannel")
                .channel(postmanChannel())
                .split()
                .route("headers['addressee'].address")
                .get();
    }
}
