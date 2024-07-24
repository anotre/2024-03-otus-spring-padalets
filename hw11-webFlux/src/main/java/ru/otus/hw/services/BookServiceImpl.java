package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.controllers.dto.BookDto;
import ru.otus.hw.controllers.dto.converter.BookDtoConverter;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Book;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.GenreRepository;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {
    private final AuthorRepository authorRepository;

    private final GenreRepository genreRepository;

    private final BookRepository bookRepository;

    private final BookDtoConverter bookDtoConverter;

    @Override
    public Mono<BookDto> findById(String id) {
        return bookRepository.findById(id).map(bookDtoConverter::toDto);
    }

    @Override
    public Flux<BookDto> findAll() {
        return bookRepository.findAll().map(bookDtoConverter::toDto);
    }

    @Override
    public Mono<BookDto> insert(String title, String authorId, String genreId) {
        return prepareBook(title, authorId, genreId)
                .flatMap(bookRepository::save)
                .map(bookDtoConverter::toDto);
    }

    @Override
    public Mono<BookDto> update(String id, String title, String authorId, String genreId) {
        return this.prepareBook(title, authorId, genreId)
                .flatMap(book -> {
                    book.setId(id);
                    return bookRepository.save(book);
                })
                .map(bookDtoConverter::toDto);
    }

    @Override
    public void deleteById(String id) {
        bookRepository.deleteById(id).subscribe();
    }

    private Mono<Book> prepareBook(String title, String authorId, String genreId) {
        var author = authorRepository.findById(authorId)
                .switchIfEmpty(Mono.error(
                        new EntityNotFoundException("Author with id %s not found".formatted(authorId))
                )).onErrorResume(exception -> Mono.empty());

        var genre = genreRepository.findById(genreId)
                .switchIfEmpty(Mono.error(
                        new EntityNotFoundException("Genre with id %s not found".formatted(genreId))
                )).onErrorResume(exception -> Mono.empty());

        return Mono.zip(Mono.just(title), author, genre)
                .map(tuple -> new Book(tuple.getT1(), tuple.getT2(), tuple.getT3()))
                .onErrorResume(throwable -> Mono.empty());
    }
}
