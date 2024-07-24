package ru.otus.hw.services;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;
import ru.otus.hw.controllers.dto.converter.GenreDtoConverter;
import ru.otus.hw.repositories.GenreRepository;

@SpringBootTest
@DisplayName("Сервис для работы с сущностями жанров")
@Import({GenreServiceImpl.class})
class GenreServiceImplTest {
    @Autowired
    private GenreService genreService;

    @Autowired
    private GenreDtoConverter genreDtoConverter;

    @Autowired
    private GenreRepository genreRepository;

    @Test
    void shouldFindAllGenres() {
        var expectedGenres = genreRepository
                .findAll()
                .map(genreDtoConverter::toDto);
        var expectedGenresCount = 3;
        var actualGenres = genreService.findAll();

        var stepVerifier = StepVerifier
                .create(Flux.zip(actualGenres, expectedGenres));

        for (int i = 0; i < expectedGenresCount; i++) {
            stepVerifier.assertNext(tuple -> Assertions.assertThat(tuple.getT1()).isEqualTo(tuple.getT2()));
        }

        stepVerifier
                .expectComplete()
                .verify();

    }
}