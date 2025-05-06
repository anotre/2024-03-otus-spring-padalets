package ru.otus.hw.services;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.controllers.dto.CommentDto;
import ru.otus.hw.controllers.dto.converter.CommentDtoConverter;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Comment;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.CommentRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;

    private final BookRepository bookRepository;

    private final CommentDtoConverter converter;

    @Override
    @Transactional(readOnly = true)
    @CircuitBreaker(name = "dbCircuitBreaker")
    public Optional<CommentDto> findById(long id) {
        return commentRepository.findById(id).map(converter::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    @CircuitBreaker(name = "dbCircuitBreaker")
    public List<CommentDto> findByBookId(long id) {
        return commentRepository.findByBookId(id).stream()
                .map(converter::toDto).toList();
    }

    @Override
    @Transactional
    @CircuitBreaker(name = "dbCircuitBreaker")
    public CommentDto insert(String text, long bookId) {
        var comment = this.save(0, text, bookId);
        return converter.toDto(comment);
    }

    @Override
    @Transactional
    @CircuitBreaker(name = "dbCircuitBreaker")
    public CommentDto update(long id, String text, long bookId) {
        var comment = this.save(id, text, bookId);
        return converter.toDto(comment);
    }

    @Override
    @Transactional
    @CircuitBreaker(name = "dbCircuitBreaker")
    public void deleteById(long id) {
        commentRepository.deleteById(id);
    }

    private Comment save(long id, String text, long bookId) {
        var book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Book with id %d not found".formatted(bookId)));
        var comment = new Comment(id, text, book);

        return commentRepository.save(comment);
    }
}
