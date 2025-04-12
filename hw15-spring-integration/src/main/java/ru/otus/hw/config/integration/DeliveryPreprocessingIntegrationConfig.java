package ru.otus.hw.config.integration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.aggregator.CorrelationStrategy;
import org.springframework.integration.aggregator.MessageCountReleaseStrategy;
import org.springframework.integration.aggregator.ReleaseStrategy;
import org.springframework.integration.dsl.IntegrationFlow;
import ru.otus.hw.service.AddresseeService;
import ru.otus.hw.service.integration.PostItemsSorter;

@Configuration
public class DeliveryPreprocessingIntegrationConfig {
    private static final int WAITING_TIMEOUT = 100;

    private static final int RELEASE_MESSAGE_COUNT = 100;

    @Bean
    public IntegrationFlow firstAddresseeFlow(AddresseeService addresseeService) {
        return f -> f.handle(addresseeService, "receive");
    }

    @Bean
    public IntegrationFlow secondAddresseeFlow(AddresseeService addresseeService) {
        return f -> f.handle(addresseeService, "receive");
    }

    @Bean
    public IntegrationFlow thirdAddresseeFlow(AddresseeService addresseeService) {
        return f -> f.handle(addresseeService, "receive");
    }

    @Bean
    public IntegrationFlow fourthAddresseeFlow(AddresseeService addresseeService) {
        return f -> f.handle(addresseeService, "receive");
    }

    @Bean
    public ReleaseStrategy messageCountReleaseStrategy() {
        return new MessageCountReleaseStrategy(RELEASE_MESSAGE_COUNT);
    }

    @Bean
    public IntegrationFlow deliveryPreprocessing(
            CorrelationStrategy correlationStrategy,
            ReleaseStrategy messageCountReleaseStrategy,
            PostItemsSorter postItemsSorter) {

        return IntegrationFlow.from("mailingInputChannel")
                .split()
                .aggregate(
                        list -> list.releaseStrategy(messageCountReleaseStrategy)
                                .correlationStrategy(correlationStrategy)
                                .groupTimeout(WAITING_TIMEOUT)
                                .expireGroupsUponCompletion(true)
                                .sendPartialResultOnExpiry(true)
                )
                .handle(postItemsSorter, "sort")
                .channel("deliveryInputChannel")
                .get();
    }
}

