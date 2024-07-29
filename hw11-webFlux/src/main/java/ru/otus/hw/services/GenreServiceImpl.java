package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import ru.otus.hw.controllers.dto.GenreDto;
import ru.otus.hw.controllers.dto.converter.GenreDtoConverter;
import ru.otus.hw.repositories.GenreRepository;

@RequiredArgsConstructor
@Service
public class GenreServiceImpl implements GenreService {
    private final GenreRepository genreRepository;

    private final GenreDtoConverter converter;

    @Override
    public Flux<GenreDto> findAll() {
        return genreRepository.findAll()
                .map(converter::toDto);
    }
}
