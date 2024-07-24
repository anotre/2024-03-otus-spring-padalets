package ru.otus.hw.controllers.rest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import ru.otus.hw.controllers.dto.GenreDto;
import ru.otus.hw.controllers.dto.converter.GenreDtoConverter;
import ru.otus.hw.models.Genre;
import ru.otus.hw.repositories.GenreRepository;
import ru.otus.hw.services.GenreService;

import java.util.List;

import static org.mockito.BDDMockito.given;

@SpringBootTest
@AutoConfigureWebTestClient
@DisplayName("REST controller for genre")
class GenreControllerTest {
    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private GenreService genreService;

    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    private GenreDtoConverter genreDtoConverter;

    private List<GenreDto> expectedGenres;

    @BeforeEach
    void setUp() {
        this.expectedGenres = Flux.fromIterable(List.of(
                        new Genre("Genre_1"),
                        new Genre("Genre_2")
                ))
                .flatMap(genreRepository::insert)
                .map(genreDtoConverter::toDto).collectList().block();
    }

    @Test
    @DisplayName("Возвращает все жанры в форме Json")
    void shouldReturnAllGenres() throws Exception {
        given(genreService.findAll())
                .willReturn(Flux.fromIterable(this.expectedGenres));

        webTestClient.get().uri("/api/v1/genres")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(GenreDto.class).isEqualTo(this.expectedGenres);
    }
}
