package ru.otus.hw.services;

import ru.otus.hw.controllers.dto.CommentDto;

import java.util.List;
import java.util.Optional;

public interface CommentService {
    Optional<CommentDto> findById(long id);

    List<CommentDto> findByBookId(long id);

    CommentDto insert(String text, long bookId);

    CommentDto update(long id, String text, long bookId);

    void deleteById(long id);
}
