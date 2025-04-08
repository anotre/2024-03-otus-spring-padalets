package ru.otus.hw.service.integration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.stereotype.Service;
import ru.otus.hw.domain.Delivery;
import ru.otus.hw.service.AddresseeService;

@Service
@Slf4j
public class AddresseeServiceImpl implements AddresseeService {
    @Override
    @ServiceActivator()
    public void receive(Delivery delivery) {
        log.info("Delivery for addressee with id {}. Post item deliveries: {}. Tread name is {}.",
                delivery.getAddressee().getId(),
                delivery.getPostItems().size(),
                Thread.currentThread().getName()
        );
    }
}
