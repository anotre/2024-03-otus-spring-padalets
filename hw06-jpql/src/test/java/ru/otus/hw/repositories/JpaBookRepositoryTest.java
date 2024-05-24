package ru.otus.hw.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@DisplayName("Репозиторий на основе Jpa для работы с сущностями книг")
@Import(JpaBookRepository.class)
class JpaBookRepositoryTest {
    private static final long EXPECTED_BOOK_ID = 1L;

    private static final long EXPECTED_NEW_BOOK_ID = 4L;

    private static final String EXPECTED_BOOK_NAME = "test-book";

    private static final List<Long> BOOK_IDS_LIST = List.of(1L, 2L, 3L);

    private static final long EXPECTED_GENRE_ID = 1L;

    private static final String EXPECTED_GENRE_NAME = "Genre_1";

    private static final long EXPECTED_AUTHOR_ID = 1L;

    private static final String EXPECTED_AUTHOR_FULL_NAME = "Author_1";

    @Autowired
    private TestEntityManager em;

    @Autowired
    private BookRepository bookRepository;

    @Test
    @DisplayName("Загружает ожидаемую книгу по ее идентификатору")
    void shouldFindExpectedBookById() {
        var actualBook = bookRepository.findById(EXPECTED_BOOK_ID);
        em.clear();
        var expectedBook = em.find(Book.class, EXPECTED_BOOK_ID);
        assertThat(actualBook).isPresent().get().isEqualTo(expectedBook);
    }

    @Test
    @DisplayName("Загружает список ожидаемых книг")
    void shouldFindExpectedBooksList() {
        var actualBooks = bookRepository.findAll();
        em.clear();
        for (int i = 0; i < BOOK_IDS_LIST.size(); i++) {
            var expectedBook = em.find(Book.class, BOOK_IDS_LIST.get(i));
            assertThat(actualBooks.get(i))
                    .usingRecursiveComparison().isEqualTo(expectedBook);
        }
    }

    @Test
    @DisplayName("Сохраняет новую комментарий")
    void shouldSaveBook() {
        var author = new Author(EXPECTED_AUTHOR_ID, EXPECTED_AUTHOR_FULL_NAME);
        var genre = new Genre(EXPECTED_GENRE_ID, EXPECTED_GENRE_NAME);
        var expectedBook = new Book(EXPECTED_NEW_BOOK_ID, EXPECTED_BOOK_NAME, author, genre);
        var newBook = bookRepository.save(new Book(0, EXPECTED_BOOK_NAME, author, genre));
        assertThat(newBook).isNotNull().usingRecursiveComparison().isEqualTo(expectedBook);
    }

    @Test
    @DisplayName("Удаляет книгу по идентификатору")
    void shouldDeleteBookById() {
        var book = em.find(Book.class, EXPECTED_BOOK_ID);
        assertThat(book).isNotNull();
        bookRepository.deleteById(EXPECTED_BOOK_ID);
        assertThat(em.find(Book.class, EXPECTED_BOOK_ID)).isNull();
    }
}