package ru.otus.hw.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.controllers.dto.AuthorDto;
import ru.otus.hw.controllers.dto.BookDto;
import ru.otus.hw.controllers.dto.GenreDto;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@DisplayName("Сервис для работы с сущностями книг")
@ComponentScan({"ru.otus.hw.repositories", "ru.otus.hw.controllers.dto"})
@Import({BookServiceImpl.class})
@Transactional(propagation = Propagation.NEVER)
class BookServiceImplTest {
    private static final long EXPECTED_BOOK_ID = 1L;

    private static final long EXPECTED_NEW_BOOK_ID = 4L;

    private static final int EXPECTED_AMOUNT_OF_BOOKS = 3;

    private static final String EXPECTED_BOOK_TITLE = "BookTitle_1";

    private static final String EXPECTED_UPDATED_BOOK_TITLE = "Updated_BookTitle_1";

    private static final long EXPECTED_AUTHOR_ID = 1L;

    private static final String EXPECTED_AUTHOR_FULL_NAME = "Author_1";

    private static final long EXPECTED_GENRE_ID = 1L;

    private static final String EXPECTED_GENRE_NAME = "Genre_1";

    private GenreDto expectedGenreDto;

    private AuthorDto expectedAuthorDto;

    private BookDto expectedBookDto;

    @Autowired
    private BookService bookService;

    @BeforeEach
    void setUp() {
        expectedAuthorDto = new AuthorDto(EXPECTED_AUTHOR_ID, EXPECTED_AUTHOR_FULL_NAME);
        expectedGenreDto = new GenreDto(EXPECTED_GENRE_ID, EXPECTED_GENRE_NAME);
        expectedBookDto = new BookDto(EXPECTED_BOOK_ID, EXPECTED_BOOK_TITLE, expectedAuthorDto, expectedGenreDto);
    }

    @Test
    @DisplayName("Загружает книгу по идентификатору")
    void shouldFindBookById() {
        var actualBookDto = bookService.findById(EXPECTED_BOOK_ID);
        assertThat(actualBookDto).isPresent().get().usingRecursiveComparison().isEqualTo(expectedBookDto);
    }

    @Test
    @DisplayName("Загружает все книги")
    void shouldFindAllBooks() {
        var actualBooksDto = bookService.findAll();
        assertThat(actualBooksDto).hasSize(EXPECTED_AMOUNT_OF_BOOKS);
    }

    @Test
    @DisplayName("Сохраняет в новую книгу")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void shouldSaveNewBook() {
        var newBookDto = bookService.insert(EXPECTED_BOOK_TITLE, EXPECTED_AUTHOR_ID, EXPECTED_GENRE_ID);
        expectedBookDto.setId(EXPECTED_NEW_BOOK_ID);
        assertThat(newBookDto).usingRecursiveComparison().isEqualTo(expectedBookDto);
    }

    @Test
    @DisplayName("Обновляет существующую книгу")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void shouldChangeExistingBook() {
        var updatedBookDto = bookService.update(
                EXPECTED_BOOK_ID,
                EXPECTED_UPDATED_BOOK_TITLE,
                EXPECTED_AUTHOR_ID,
                EXPECTED_GENRE_ID);
        expectedBookDto.setTitle(EXPECTED_UPDATED_BOOK_TITLE);
        assertThat(updatedBookDto).usingRecursiveComparison().isEqualTo(expectedBookDto);
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