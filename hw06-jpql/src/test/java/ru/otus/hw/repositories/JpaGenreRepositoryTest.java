package ru.otus.hw.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.otus.hw.models.Genre;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@DisplayName("Репозиторий на основе Jpa для работы с сущностями жанров")
@Import(JpaGenreRepository.class)
class JpaGenreRepositoryTest {
    private static final long EXPECTED_GENRE_ID = 1L;

    private static final int EXPECTED_NUMBER_OF_GENRES = 3;

    private static final List<Long> GENRE_IDS_LIST = List.of(1L, 2L, 3L);

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
        var actualGenres = genreRepository.findAll();

        for (int i = 0; i < GENRE_IDS_LIST.size(); i++) {
            var expectedGenre = em.find(Genre.class, GENRE_IDS_LIST.get(i));
            assertThat(actualGenres.get(i))
                    .usingRecursiveComparison().isEqualTo(expectedGenre);
        }
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