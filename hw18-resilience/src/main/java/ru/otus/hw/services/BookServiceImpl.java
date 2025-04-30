package ru.otus.hw.services;

import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.controllers.dto.BookDto;
import ru.otus.hw.controllers.dto.converter.BookDtoConverter;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Book;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.GenreRepository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static ru.otus.hw.config.ResilienceConfig.DB_CIRCUIT_BREAKER;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {
    private final AuthorRepository authorRepository;

    private final GenreRepository genreRepository;

    private final BookRepository bookRepository;

    private final BookDtoConverter bookDtoConverter;

    @Override
    @Transactional(readOnly = true)
    @CircuitBreaker(name = DB_CIRCUIT_BREAKER, fallbackMethod = "fallback")
    public Optional<BookDto> findById(long id) {
        return bookRepository.findById(id).map(bookDtoConverter::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    @CircuitBreaker(name = DB_CIRCUIT_BREAKER, fallbackMethod = "fallbackList")
    public List<BookDto> findAll() {
        return bookRepository.findAll().stream().map(bookDtoConverter::toDto).toList();
    }

    @Override
    @Transactional
    @CircuitBreaker(name = DB_CIRCUIT_BREAKER)
    public BookDto insert(String title, long authorId, long genreId) {
        var book = save(0, title, authorId, genreId);
        return bookDtoConverter.toDto(book);
    }

    @Override
    @Transactional
    @CircuitBreaker(name = DB_CIRCUIT_BREAKER)
    public BookDto update(long id, String title, long authorId, long genreId) {
        var book = save(id, title, authorId, genreId);
        return bookDtoConverter.toDto(book);
    }

    @Override
    @Transactional
    @CircuitBreaker(name = DB_CIRCUIT_BREAKER)
    public void deleteById(long id) {
        bookRepository.deleteById(id);
    }

    private Book save(long id, String title, long authorId, long genreId) {
        var author = authorRepository.findById(authorId)
                .orElseThrow(() -> new EntityNotFoundException("Author with id %d not found".formatted(authorId)));
        var genre = genreRepository.findById(genreId)
                .orElseThrow(() -> new EntityNotFoundException("Genre with id %d not found".formatted(genreId)));
        var book = new Book(id, title, author, genre);

        return bookRepository.save(book);
    }

    private Optional<BookDto> fallback(Long id, CallNotPermittedException exception) {
        return Optional.empty();
    }

    private List<BookDto> fallbackList(CallNotPermittedException exception) {
        return Collections.emptyList();
    }
}
