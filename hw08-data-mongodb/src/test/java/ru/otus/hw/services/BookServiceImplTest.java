package ru.otus.hw.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import ru.otus.hw.events.BookModelListener;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
@DisplayName("Сервис для работы с сущностями книг")
@Import({BookServiceImpl.class, BookModelListener.class})
class BookServiceImplTest {
    private static final String EXPECTED_BOOK_ID = "1";

    private static final int EXPECTED_AMOUNT_OF_BOOKS = 3;

    private static final String EXPECTED_BOOK_TITLE = "BookTitle_1";

    private static final String EXPECTED_UPDATED_BOOK_TITLE = "Updated_BookTitle_1";

    private static final String EXPECTED_AUTHOR_ID = "1";

    private static final String EXPECTED_AUTHOR_FULL_NAME = "Author_1";

    private static final String EXPECTED_GENRE_ID = "1";

    private static final String EXPECTED_GENRE_NAME = "Genre_1";

    private Genre expectedGenre;

    private Author expectedAuthor;

    private Book expectedBook;

    @Autowired
    private BookService bookService;

    @BeforeEach
    void setUp() {
        expectedAuthor = new Author(EXPECTED_AUTHOR_ID, EXPECTED_AUTHOR_FULL_NAME);
        expectedGenre = new Genre(EXPECTED_GENRE_ID, EXPECTED_GENRE_NAME);
        expectedBook = new Book(EXPECTED_BOOK_ID, EXPECTED_BOOK_TITLE, expectedAuthor, expectedGenre);
    }

    @Test
    @DisplayName("Загружает книгу по идентификатору")
    void shouldFindBookById() {
        var actualBook = bookService.findById(EXPECTED_BOOK_ID);
        assertThat(actualBook).isPresent().get().usingRecursiveComparison().isEqualTo(expectedBook);
    }

    @Test
    @DisplayName("Загружает все книги")
    void shouldFindAllBooks() {
        var actualBook = bookService.findAll();
        assertThat(actualBook).hasSize(EXPECTED_AMOUNT_OF_BOOKS);
    }

    @Test
    @DisplayName("Сохраняет в новую книгу")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void shouldSaveNewBook() {
        var newBook = bookService.insert(EXPECTED_BOOK_TITLE, EXPECTED_AUTHOR_ID, EXPECTED_GENRE_ID);
        var expectedBook = bookService.findById(newBook.getId());
        assertThat(expectedBook).isPresent();
        assertThat(newBook).usingRecursiveComparison().isEqualTo(expectedBook.get());
    }

    @Test
    @DisplayName("Обновляет существующую книгу")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void shouldChangeExistingBook() {
        var updatedBook = bookService.update(
                EXPECTED_BOOK_ID,
                EXPECTED_UPDATED_BOOK_TITLE,
                EXPECTED_AUTHOR_ID,
                EXPECTED_GENRE_ID);
        var expectedBook = bookService.findById(EXPECTED_BOOK_ID);
        assertThat(expectedBook).isPresent();
        assertThat(updatedBook).usingRecursiveComparison().isEqualTo(expectedBook.get());
    }

    @Test
    @DisplayName("Удаляет книгу по идентификатору")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void shouldDeleteExistingBookById() {
        assertThat(bookService.findById(EXPECTED_BOOK_ID)).isPresent();
        bookService.deleteById(EXPECTED_BOOK_ID);
        assertThat(bookService.findById(EXPECTED_BOOK_ID)).isNotPresent();
    }
}