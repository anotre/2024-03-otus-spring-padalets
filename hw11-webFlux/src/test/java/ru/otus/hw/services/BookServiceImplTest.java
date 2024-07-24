package ru.otus.hw.services;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;
import ru.otus.hw.controllers.dto.converter.BookDtoConverter;
import ru.otus.hw.events.BookModelListener;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.CommentRepository;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@DisplayName("Сервис для работы с сущностями книг")
@Import({BookServiceImpl.class, BookModelListener.class})
class BookServiceImplTest {
    private static final String EXPECTED_BOOK_ID = "testBookId";

    private static final String EXPECTED_BOOK_TITLE = "BookTitle_1";

    private static final String EXPECTED_UPDATED_BOOK_TITLE = "Updated_BookTitle_1";

    private static final String EXPECTED_AUTHOR_ID = "testAuthorId";

    private static final String EXPECTED_GENRE_ID = "testGenreId";

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private BookService bookService;

    @Autowired
    private BookDtoConverter bookDtoConverter;

    @Autowired
    private CommentRepository commentRepository;

    @Test
    @DisplayName("Загружает книгу по идентификатору")
    void shouldFindBookById() {
        var expectedBook = bookRepository
                .findById(EXPECTED_BOOK_ID)
                .map(book -> bookDtoConverter.toDto(book)).block();

        var actualBook = bookService.findById(EXPECTED_BOOK_ID);

        StepVerifier
                .create(actualBook)
                .expectNext(expectedBook)
                .expectComplete()
                .verify();
    }

    @Test
    @DisplayName("Загружает все книги")
    void shouldFindAllBooks() {
        var expectedBooksCount = 3;
        var expectedBooks = bookRepository.findAll().map(book -> bookDtoConverter.toDto(book));
        var actualBooks = bookService.findAll();

        var stepVerifier = StepVerifier.create(Flux.zip(actualBooks, expectedBooks));

        for (int i = 0; i < expectedBooksCount; i++) {
            stepVerifier.assertNext(tuple -> assertThat(tuple.getT1()).isEqualTo(tuple.getT2()));
        }

        stepVerifier
                .expectComplete()
                .verify();
    }

    @Test
    @DisplayName("Сохраняет в новую книгу")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void shouldSaveNewBook() {
        var newBook = bookService.insert(EXPECTED_BOOK_TITLE, EXPECTED_AUTHOR_ID, EXPECTED_GENRE_ID).flatMap(bookDto -> bookRepository.findById(bookDto.getId()));

        StepVerifier
                .create(newBook)
                .assertNext(book -> {
                    assertThat(book.getId()).isNotNull();
                    assertThat(book.getTitle()).isEqualTo(EXPECTED_BOOK_TITLE);
                })
                .expectComplete()
                .verify();
    }

    @Test
    @DisplayName("Обновляет существующую книгу")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void shouldChangeExistingBook() {
        var updatedBook = bookService.update(
                        EXPECTED_BOOK_ID,
                        EXPECTED_UPDATED_BOOK_TITLE,
                        EXPECTED_AUTHOR_ID,
                        EXPECTED_GENRE_ID)
                .flatMap(book -> bookRepository.findById(book.getId()));

        StepVerifier.create(updatedBook).assertNext(book -> {
                    assertThat(book.getId()).isEqualTo(EXPECTED_BOOK_ID);
                    assertThat(book.getTitle()).isEqualTo(EXPECTED_UPDATED_BOOK_TITLE);
                })
                .expectComplete()
                .verify();
    }

    @Test
    @DisplayName("Удаляет книгу по идентификатору")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void shouldDeleteExistingBookById() {
        var expectedEmptyBook = bookRepository
                .findById(EXPECTED_BOOK_ID)
                .doOnNext(book -> bookService.deleteById(book.getId()))
                .flatMap(book -> bookRepository.findById(book.getId()));

        StepVerifier
                .create(expectedEmptyBook)
                .expectNextCount(0)
                .expectComplete()
                .verify();

        StepVerifier
                .create(commentRepository.findByBookId(EXPECTED_BOOK_ID))
                .expectNextCount(0)
                .expectComplete()
                .verify();
    }
}