package ru.otus.hw.services;

import ru.otus.hw.controllers.dto.AuthorDto;

import java.util.List;

public interface AuthorService {
    List<AuthorDto> findAll();
}
