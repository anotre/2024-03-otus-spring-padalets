package ru.otus.hw.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.otus.hw.models.Author;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@DisplayName("Репозиторий на основе Jpa для работы с сущностями авторов")
class JpaAuthorRepositoryTest {
    private static final long EXPECTED_AUTHOR_ID = 1L;

    private static final int EXPECTED_NUMBER_OF_AUTHORS = 3;

    @Autowired
    private TestEntityManager em;

    @Autowired
    private AuthorRepository authorRepository;

    @Test
    @DisplayName("Загружает ожидаемое количество авторов")
    void shouldFindFullAuthorsList() {
        var actualAuthors = authorRepository.findAll();
        assertThat(actualAuthors).hasSize(EXPECTED_NUMBER_OF_AUTHORS);
    }

    @Test
    @DisplayName("Загружает список ожидаемых авторов")
    void shouldFindExpectedAuthorsList() {
        List<Author> expectedAuthors = List.of(
                new Author(1L, "Author_1"),
                new Author(2L, "Author_2"),
                new Author(3L, "Author_3")
        );
        var actualAuthors = authorRepository.findAll();
        assertThat(actualAuthors).hasSize(expectedAuthors.size())
                .containsExactlyInAnyOrder(
                        expectedAuthors.get(0),
                        expectedAuthors.get(1),
                        expectedAuthors.get(2));
    }

    @Test
    @DisplayName("Загружает ожидаемого автора")
    void shouldFindExpectedAuthorById() {
        var actualAuthor = authorRepository.findById(EXPECTED_AUTHOR_ID);
        em.clear();
        var expectedAuthor = em.find(Author.class, EXPECTED_AUTHOR_ID);
        assertThat(actualAuthor).isPresent().get().usingRecursiveComparison().isEqualTo(expectedAuthor);
    }
}