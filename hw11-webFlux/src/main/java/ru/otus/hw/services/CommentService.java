package ru.otus.hw.services;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.controllers.dto.CommentDto;

public interface CommentService {
    Mono<CommentDto> findById(String id);

    Flux<CommentDto> findByBookId(String id);

    Mono<CommentDto> insert(String text, String bookId);

    Mono<CommentDto> update(String id, String text, String bookId);

    void deleteById(String id);
}
