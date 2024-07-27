package ru.otus.hw.controllers.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import reactor.core.publisher.Mono;
import ru.otus.hw.controllers.dto.AuthorDto;
import ru.otus.hw.controllers.dto.BookDto;
import ru.otus.hw.controllers.dto.GenreDto;
import ru.otus.hw.services.BookServiceImpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@SpringBootTest
@AutoConfigureWebTestClient
@DisplayName("REST controller for books")
class BookControllerTest {
    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private BookServiceImpl bookService;

    public static final String ERROR_MESSAGE_OUT_OF_SIZE_TITLE = "Title field should be between 2 and 255 characters";

    public static final String ERROR_MESSAGE_BLANK_AUTHOR = "Author field should not be blank";

    public static final String ERROR_MESSAGE_BLANK_GENRE = "Genre field should not be blank";

    public static final Map<String, String> EXPECTED_ERROR_MESSAGES = new HashMap<>() {{
        put("author", ERROR_MESSAGE_BLANK_AUTHOR);
        put("genre", ERROR_MESSAGE_BLANK_GENRE);
        put("title", ERROR_MESSAGE_OUT_OF_SIZE_TITLE);
    }};

    private BookDto bookDto;

    private GenreDto genreDto;

    private AuthorDto authorDto;

    @Autowired
    private ObjectMapper mapper;

    @BeforeEach
    void setUp() {
        this.genreDto = new GenreDto("Genre_1", "Genre_1");
        this.authorDto = new AuthorDto("Author_1", "Author_1");
        this.bookDto = new BookDto("Book_1", "BookTitle_1", authorDto, genreDto);
    }

    @Test
    @DisplayName("Возвращает книги в формате json")
    void shouldReturnExpectedBooks() throws Exception {
        given(bookService.findAll()).willReturn(Flux.fromIterable(List.of(this.bookDto)));

        webTestClient.get().uri("/api/v1/books")
                .accept(APPLICATION_JSON)
                .exchange()
                .expectHeader().contentType(APPLICATION_JSON)
                .expectStatus().isOk()
                .expectBodyList(BookDto.class).isEqualTo(List.of(bookDto));
    }

    @Test
    @DisplayName("Возвращает книгу в формате json")
    void shouldReturnExpectedBook() {
        given(bookService.findById(bookDto.getId())).willReturn(Mono.just(this.bookDto));

        webTestClient.get().uri("/api/v1/books/" + bookDto.getId())
                .accept(APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(APPLICATION_JSON)
                .expectBody(BookDto.class).isEqualTo(this.bookDto);
    }

    @Test
    @DisplayName("Создает книгу по данным из тела запроса, возвращает 201 статус")
    void shouldCreateBookWithExpectedResponseStatus() {
        var expectedNewBook = new BookDto(null, "new book title", this.authorDto, this.genreDto);

        given(bookService.insert(
                expectedNewBook.getTitle(),
                expectedNewBook.getAuthor().getId(),
                expectedNewBook.getGenre().getId()
        )).willReturn(Mono.just(expectedNewBook));

        webTestClient.post().uri("/api/v1/books")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .bodyValue(expectedNewBook)
                .exchange()
                .expectStatus().isCreated()
                .expectBody().isEmpty();
    }

    @Test
    @DisplayName("Изменяет книгу по данным из тела запроса")
    void shouldUpdateBookWithExpectedResponseStatus() {
        this.bookDto.setTitle("updated_book_title");
        given(bookService.update(
                this.bookDto.getId(),
                this.bookDto.getTitle(),
                this.bookDto.getAuthor().getId(),
                this.bookDto.getGenre().getId()
        )).willReturn(Mono.just(this.bookDto));

        webTestClient.patch().uri("/api/v1/books")
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .bodyValue(this.bookDto)
                .exchange()
                .expectStatus().isOk()
                .expectBody(BookDto.class).isEqualTo(this.bookDto);
    }

    @Test
    @DisplayName("Удаляет книгу, возвращает 204 статус")
    void shouldDeleteBookWithExpectedResponseStatus() {
        webTestClient.delete().uri("/api/v1/books/" + this.bookDto.getId())
                .exchange()
                .expectStatus().isNoContent()
                .expectBody().isEmpty();
    }

    @Test
    @DisplayName("Возвращает сообщения об ошибках при создании книги по некорректным данным")
    void shouldReturnErrorsMessagesOnInvalidPost() throws Exception {
        var invalidBook = new BookDto(null, "i", null, null);

        webTestClient.post().uri("/api/v1/books")
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .bodyValue(invalidBook)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(String.class).isEqualTo(mapper.writeValueAsString(EXPECTED_ERROR_MESSAGES));
    }

    @Test
    @DisplayName("Возвращает сообщения об ошибках при изменении книги с некорректными данными")
    void shouldReturnErrorsMessagesOnInvalidPatch() throws Exception {
        var invalidBook = new BookDto("invalidBookId", "a", null, null);
        webTestClient.patch().uri("/api/v1/books")
                .contentType(APPLICATION_JSON)
                .bodyValue(invalidBook)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(String.class).isEqualTo(mapper.writeValueAsString(EXPECTED_ERROR_MESSAGES));
    }

    @Test
    @DisplayName("Возращает 404 статус если запрашиваемая книга не существует")
    void shouldReturnNotFoundStatus() {
        var nonExistentBookId = "nonExistentBookId";
        given(bookService.findById(nonExistentBookId)).willReturn(Mono.empty());
        webTestClient.get().uri("/api/v1/books/" + nonExistentBookId)
                .accept(APPLICATION_JSON)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody().isEmpty();
    }
}