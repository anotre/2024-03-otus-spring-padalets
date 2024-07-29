package ru.otus.hw.controllers.rest;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.scheduler.Scheduler;
import ru.otus.hw.controllers.dto.AuthorDto;
import ru.otus.hw.services.AuthorService;

@RestController
@RequiredArgsConstructor
public class AuthorController {
    private final AuthorService authorService;

    private final Scheduler workerPool;

    @GetMapping("/api/v1/authors")
    public Flux<AuthorDto> getAllAuthors() {
        return authorService.findAll().subscribeOn(workerPool);
    }
}
