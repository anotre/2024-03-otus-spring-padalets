package ru.otus.hw.services;

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

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {
    private final AuthorRepository authorRepository;

    private final GenreRepository genreRepository;

    private final BookRepository bookRepository;

    private final BookDtoConverter bookDtoConverter;

    @Override
    @Transactional(readOnly = true)
    @CircuitBreaker(name = "dbCircuitBreaker")
    public Optional<BookDto> findById(long id) {
        return bookRepository.findById(id).map(bookDtoConverter::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    @CircuitBreaker(name = "dbCircuitBreaker")
    public List<BookDto> findAll() {
        return bookRepository.findAll().stream().map(bookDtoConverter::toDto).toList();
    }

    @Override
    @Transactional
    @CircuitBreaker(name = "dbCircuitBreaker")
    public BookDto insert(String title, long authorId, long genreId) {
        var book = save(0, title, authorId, genreId);
        return bookDtoConverter.toDto(book);
    }

    @Override
    @Transactional
    @CircuitBreaker(name = "dbCircuitBreaker")
    public BookDto update(long id, String title, long authorId, long genreId) {
        var book = save(id, title, authorId, genreId);
        return bookDtoConverter.toDto(book);
    }

    @Override
    @Transactional
    @CircuitBreaker(name = "dbCircuitBreaker")
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
}
