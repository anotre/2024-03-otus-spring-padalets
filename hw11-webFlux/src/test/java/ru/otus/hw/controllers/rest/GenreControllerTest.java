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

    private List<GenreDto> expectedGenres;

    @BeforeEach
    void setUp() {
        this.expectedGenres = List.of(
                new GenreDto("Genre_1_Id", "Genre_1"),
                new GenreDto("Genre_2_Id", "Genre_2")
        );
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
