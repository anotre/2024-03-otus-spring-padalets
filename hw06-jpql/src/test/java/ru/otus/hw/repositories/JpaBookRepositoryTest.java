package ru.otus.hw.repositories;

import org.junit.jupiter.api.BeforeEach;
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

    private static final String EXPECTED_BOOK_TITLE = "BookTitle_1";

    private static final String EXPECTED_BOOK_UPDATED_TITLE = "Updated-BookTitle_1";

    private static final List<Long> EXPECTED_BOOK_IDS_LIST = List.of(1L, 2L, 3L);

    private static final long EXPECTED_GENRE_ID = 1L;

    private static final long EXPECTED_AUTHOR_ID = 1L;

    private Genre expectedGenre;

    private Author expectedAuthor;

    private Book expectedBook;

    @Autowired
    private TestEntityManager em;

    @Autowired
    private BookRepository bookRepository;

    @BeforeEach
    void setUp() {
        expectedAuthor = em.find(Author.class, EXPECTED_AUTHOR_ID);
        expectedGenre = em.find(Genre.class, EXPECTED_GENRE_ID);
        expectedBook = new Book(EXPECTED_BOOK_ID, EXPECTED_BOOK_TITLE, expectedAuthor, expectedGenre);

    }

    @Test
    @DisplayName("Загружает ожидаемую книгу по ее идентификатору")
    void shouldFindExpectedBookById() {
        var actualBook = bookRepository.findById(EXPECTED_BOOK_ID);
        em.clear();
        assertThat(actualBook).isPresent().get().isEqualTo(expectedBook);
    }

    @Test
    @DisplayName("Загружает список ожидаемых книг")
    void shouldFindExpectedBooksList() {
        assertThat(bookRepository.findAll())
                .hasSize(EXPECTED_BOOK_IDS_LIST.size())
                .allMatch(b -> EXPECTED_BOOK_IDS_LIST.contains(b.getId()));
    }

    @Test
    @DisplayName("Сохраняет новую комментарий")
    void shouldSaveBook() {
        var expectedBook = new Book(EXPECTED_NEW_BOOK_ID, EXPECTED_BOOK_TITLE, expectedAuthor, expectedGenre);
        var savedBook = bookRepository.save(new Book(0, EXPECTED_BOOK_TITLE, expectedAuthor, expectedGenre));
        em.detach(savedBook);
        assertThat(em.find(Book.class, EXPECTED_NEW_BOOK_ID)).isNotNull()
                .usingRecursiveComparison().isEqualTo(expectedBook);
    }

    @Test
    @DisplayName("Обновляет существующую комментарий")
    void shouldUpdateBook() {
        var updatedBook = bookRepository.save(
                new Book(EXPECTED_BOOK_ID, EXPECTED_BOOK_UPDATED_TITLE, expectedAuthor, expectedGenre));
        em.flush();
        em.detach(updatedBook);
        expectedBook.setTitle(EXPECTED_BOOK_UPDATED_TITLE);
        assertThat(em.find(Book.class, EXPECTED_BOOK_ID)).isNotNull().usingRecursiveComparison().isEqualTo(expectedBook);
    }

    @Test
    @DisplayName("Удаляет книгу по идентификатору")
    void shouldDeleteBookById() {
        var book = em.find(Book.class, EXPECTED_BOOK_ID);
        assertThat(book).isNotNull();
        bookRepository.deleteById(EXPECTED_BOOK_ID);
        em.flush();
        em.detach(book);
        assertThat(em.find(Book.class, EXPECTED_BOOK_ID)).isNull();
    }
}