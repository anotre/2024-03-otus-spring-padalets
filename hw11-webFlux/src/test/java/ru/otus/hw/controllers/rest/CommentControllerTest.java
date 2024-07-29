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
import ru.otus.hw.controllers.dto.CommentDto;
import ru.otus.hw.services.CommentService;

import java.util.List;

import static org.mockito.BDDMockito.given;

@SpringBootTest
@AutoConfigureWebTestClient
@DisplayName("REST controller for comments")
class CommentControllerTest {
    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private CommentService commentService;

    private List<CommentDto> expectedComments;

    private static final String BOOK_ID = "bookId";

    @BeforeEach
    void setUp() {
        this.expectedComments = List.of(
                new CommentDto("testCommentId", "Comment_1", null),
                new CommentDto("testCommentId", "Comment_2", null)
        );
    }

    @Test
    @DisplayName("Возвращает все комментарии по переданному идентификатору книги")
    void shouldReturnAllCommentsByBookId() {
        given(commentService.findByBookId(BOOK_ID)).willReturn(Flux.fromIterable(expectedComments));

        webTestClient.get().uri(String.format("/api/v1/comments/book/%s", BOOK_ID))
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(CommentDto.class).isEqualTo(this.expectedComments);
    }
}