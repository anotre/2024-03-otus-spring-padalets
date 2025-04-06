package ru.otus.hw.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import ru.otus.hw.service.PostOfficeService;

@Component
@RequiredArgsConstructor
public class ApplicationRunner implements CommandLineRunner {
    private final PostOfficeService postOfficeService;

    @Override
    public void run(String... args) {
        postOfficeService.doWork();
    }
}
