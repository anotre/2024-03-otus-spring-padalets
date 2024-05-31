package ru.otus.hw.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Genre;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@DisplayName("Репозиторий на основе Jpa для работы с сущностями жанров")
@Import(JpaGenreRepository.class)
class JpaGenreRepositoryTest {
    private static final long EXPECTED_GENRE_ID = 1L;

    private static final int EXPECTED_NUMBER_OF_GENRES = 3;

    @Autowired
    private TestEntityManager em;

    @Autowired
    private GenreRepository genreRepository;


    @Test
    @DisplayName("Загружает ожидаемое количество жанров")
    void shouldFindFullGenresList() {
        var actualGenres = genreRepository.findAll();
        assertThat(actualGenres).hasSize(EXPECTED_NUMBER_OF_GENRES);
    }

    @Test
    @DisplayName("Загружает список ожидаемых жанров")
    void shouldFindExpectedGenresList() {
        List<Genre> expectedGenres = List.of(
                new Genre(1L, "Genre_1"),
                new Genre(2L, "Genre_2"),
                new Genre(3L, "Genre_3")
        );
        var actualGenres = genreRepository.findAll();
        assertThat(actualGenres).hasSize(expectedGenres.size())
                .containsExactlyInAnyOrder(
                        expectedGenres.get(0),
                        expectedGenres.get(1),
                        expectedGenres.get(2));
    }

    @Test
    @DisplayName("Загружает ожидаемый жанр")
    void shouldFindExpectedGenresById() {
        var actualGenre = genreRepository.findById(EXPECTED_GENRE_ID);
        var expectedGenre = em.find(Genre.class, EXPECTED_GENRE_ID);
        assertThat(actualGenre).isPresent().get().usingRecursiveComparison()
                .isEqualTo(expectedGenre);
    }
}