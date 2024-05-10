package ru.otus.hw.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.otus.hw.models.Genre;

import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе Jdbc для работы с жанрами")
@JdbcTest
@Import({JdbcGenreRepository.class})
class JdbcGenreRepositoryTest {
    @Autowired
    private JdbcGenreRepository genreRepository;

    @DisplayName("Должен предоставлять список всех жанров")
    @Test
    void shouldReturnCorrectGenreList() {
        var actualList = genreRepository.findAll();
        assertThat(actualList).containsExactlyElementsOf(getDbGenres());
    }

    @DisplayName("Должен предоставлять жанр по id")
    @ParameterizedTest
    @MethodSource("getDbGenres")
    void shouldReturnCorrectGenreById(Genre expectedGenre) {
        var actualGenre = genreRepository.findById(expectedGenre.getId());
        assertThat(actualGenre).hasValue(expectedGenre);
    }

    private static List<Genre> getDbGenres() {
        return IntStream.range(1, 4).boxed()
                .map(id -> new Genre(id, "Genre_" + id))
                .toList();
    }
}