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
import ru.otus.hw.controllers.dto.AuthorDto;
import ru.otus.hw.services.AuthorService;

import java.util.List;

import static org.mockito.BDDMockito.given;

@SpringBootTest
@AutoConfigureWebTestClient
@DisplayName("REST controller for authors")
class AuthorControllerTest {
    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private AuthorService authorService;

    private List<AuthorDto> expectedAuthors;

    @BeforeEach
    void setUp() {
        this.expectedAuthors = List.of(
                new AuthorDto("Author_1_Id", "Author_1"),
                new AuthorDto("Author_2_Id", "Author_2")
        );
    }

    @Test
    @DisplayName("Возвращает все жанры в форме Json")
    void shouldReturnAllAuthors() throws Exception {
        given(authorService.findAll())
                .willReturn(Flux.fromIterable(this.expectedAuthors));

        webTestClient.get().uri("/api/v1/authors")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(AuthorDto.class).isEqualTo(this.expectedAuthors);
    }
}