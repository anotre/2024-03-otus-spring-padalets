package ru.otus.hw.services;

import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
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

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static ru.otus.hw.config.ResilienceConfig.DB_CIRCUIT_BREAKER;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;

    private final BookRepository bookRepository;

    private final CommentDtoConverter converter;

    @Override
    @Transactional(readOnly = true)
    @CircuitBreaker(name = DB_CIRCUIT_BREAKER, fallbackMethod = "fallback")
    public Optional<CommentDto> findById(long id) {
        return commentRepository.findById(id).map(converter::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    @CircuitBreaker(name = DB_CIRCUIT_BREAKER, fallbackMethod = "fallbackList")
    public List<CommentDto> findByBookId(long id) {
        return commentRepository.findByBookId(id).stream()
                .map(converter::toDto).toList();
    }

    @Override
    @Transactional
    @CircuitBreaker(name = DB_CIRCUIT_BREAKER)
    public CommentDto insert(String text, long bookId) {
        var comment = this.save(0, text, bookId);
        return converter.toDto(comment);
    }

    @Override
    @Transactional
    @CircuitBreaker(name = DB_CIRCUIT_BREAKER)
    public CommentDto update(long id, String text, long bookId) {
        var comment = this.save(id, text, bookId);
        return converter.toDto(comment);
    }

    @Override
    @Transactional
    @CircuitBreaker(name = DB_CIRCUIT_BREAKER)
    public void deleteById(long id) {
        commentRepository.deleteById(id);
    }

    private Comment save(long id, String text, long bookId) {
        var book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Book with id %d not found".formatted(bookId)));
        var comment = new Comment(id, text, book);

        return commentRepository.save(comment);
    }

    private Optional<CommentDto> fallback(Long id, CallNotPermittedException exception) {
        return Optional.empty();
    }

    private List<CommentDto> fallbackList(CallNotPermittedException exception) {
        return Collections.emptyList();
    }
}
