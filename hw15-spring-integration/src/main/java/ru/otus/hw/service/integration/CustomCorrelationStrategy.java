package ru.otus.hw.service.integration;

import org.springframework.integration.aggregator.CorrelationStrategy;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;
import ru.otus.hw.domain.PostItem;

@Component
public class CustomCorrelationStrategy implements CorrelationStrategy {
    @Override
    public Object getCorrelationKey(Message<?> message) {
        return message.getPayload() instanceof PostItem;
    }
}
