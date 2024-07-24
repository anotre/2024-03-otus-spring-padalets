package ru.otus.hw.services;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.controllers.dto.BookDto;

public interface BookService {
    Mono<BookDto> findById(String id);

    Flux<BookDto> findAll();

    Mono<BookDto> insert(String title, String authorId, String genreId);

    Mono<BookDto> update(String id, String title, String authorId, String genreId);

    void deleteById(String id);
}
