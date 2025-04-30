package ru.otus.hw.services;

import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.controllers.dto.AuthorDto;
import ru.otus.hw.controllers.dto.GenreDto;
import ru.otus.hw.controllers.dto.converter.GenreDtoConverter;
import ru.otus.hw.repositories.GenreRepository;

import java.util.Collections;
import java.util.List;

import static ru.otus.hw.config.ResilienceConfig.DB_CIRCUIT_BREAKER;

@RequiredArgsConstructor
@Service
public class GenreServiceImpl implements GenreService {
    private final GenreRepository genreRepository;

    private final GenreDtoConverter converter;

    @Override
    @Transactional(readOnly = true)
    @CircuitBreaker(name = DB_CIRCUIT_BREAKER, fallbackMethod = "fallbackList")
    public List<GenreDto> findAll() {
        return genreRepository.findAll().stream()
                .map(converter::toDto).toList();
    }

    private List<AuthorDto> fallbackList(CallNotPermittedException exception) {
        return Collections.emptyList();
    }
}
