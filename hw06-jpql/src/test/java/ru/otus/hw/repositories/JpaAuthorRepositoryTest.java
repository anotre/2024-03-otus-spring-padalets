package ru.otus.hw.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.otus.hw.models.Author;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@DisplayName("Репозиторий на основе Jpa для работы с сущностями авторов")
@Import(JpaAuthorRepository.class)
class JpaAuthorRepositoryTest {
    private static final long EXPECTED_AUTHOR_ID = 1L;

    private static final int EXPECTED_NUMBER_OF_AUTHORS = 3;

    private static final List<Long> AUTHOR_IDS_LIST = List.of(1L, 2L, 3L);

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
        var actualAuthors = authorRepository.findAll();
        em.clear();

        for (int i = 0; i < AUTHOR_IDS_LIST.size(); i++) {
            var expectedAuthor = em.find(Author.class, AUTHOR_IDS_LIST.get(i));
            assertThat(actualAuthors.get(i)).usingRecursiveComparison().isEqualTo(expectedAuthor);
        }
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