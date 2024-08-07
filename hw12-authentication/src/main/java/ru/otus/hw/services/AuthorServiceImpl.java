package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.controllers.dto.AuthorDto;
import ru.otus.hw.controllers.dto.converter.AuthorDtoConverter;
import ru.otus.hw.repositories.AuthorRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthorServiceImpl implements AuthorService {
    private final AuthorRepository authorRepository;

    private final AuthorDtoConverter converter;

    @Override
    public List<AuthorDto> findAll() {
        return authorRepository.findAll().stream()
                .map(converter::toDto).toList();
    }
}
