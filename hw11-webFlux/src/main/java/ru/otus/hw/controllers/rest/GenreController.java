package ru.otus.hw.controllers.rest;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.scheduler.Scheduler;
import ru.otus.hw.controllers.dto.GenreDto;
import ru.otus.hw.services.GenreService;

@RestController
@RequiredArgsConstructor
public class GenreController {
    private final GenreService genreService;

    private final Scheduler workerPool;

    @GetMapping("/api/v1/genres")
    public Flux<GenreDto> getAllGenres() {
        return genreService.findAll().subscribeOn(workerPool);
    }
}
