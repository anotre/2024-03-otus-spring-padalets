package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import ru.otus.hw.controllers.dto.AuthorDto;
import ru.otus.hw.controllers.dto.converter.AuthorDtoConverter;
import ru.otus.hw.repositories.AuthorRepository;

@Service
@RequiredArgsConstructor
public class AuthorServiceImpl implements AuthorService {
    private final AuthorRepository authorRepository;

    private final AuthorDtoConverter converter;

    @Override
    public Flux<AuthorDto> findAll() {
        return authorRepository.findAll()
                .map(converter::toDto);
    }
}
