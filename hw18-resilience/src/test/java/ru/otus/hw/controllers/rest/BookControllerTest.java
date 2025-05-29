package ru.otus.hw.controllers.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.controllers.dto.AuthorDto;
import ru.otus.hw.controllers.dto.BookDto;
import ru.otus.hw.controllers.dto.GenreDto;
import ru.otus.hw.services.AuthorService;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.CommentService;
import ru.otus.hw.services.GenreService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookController.class)
@DisplayName("REST controller for books")
class BookControllerTest {
    public static final long EXPECTED_BOOK_ID = 1L;

    public static final long NON_EXISTENT_BOOK_ID = 99L;

    public static final String EXPECTED_UPDATED_BOOK_TITLE = "updated_book_title";

    public static final String ERROR_MESSAGE_OUT_OF_SIZE_TITLE = "Title field should be between 2 and 255 characters";

    public static final String ERROR_MESSAGE_BLANK_AUTHOR = "Author field should not be blank";

    public static final String ERROR_MESSAGE_BLANK_GENRE = "Genre field should not be blank";

    public static final Map<String, String> EXPECTED_ERROR_MESSAGES = new HashMap<>() {{
        put("author", ERROR_MESSAGE_BLANK_AUTHOR);
        put("genre", ERROR_MESSAGE_BLANK_GENRE);
        put("title", ERROR_MESSAGE_OUT_OF_SIZE_TITLE);
    }};

    @MockBean
    private BookService bookService;

    @MockBean
    private CommentService commentService;

    @MockBean
    private AuthorService authorService;

    @MockBean
    private GenreService genreService;

    private BookDto bookDto;

    private String expectedBookAsString;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @BeforeEach
    void setUp() throws Exception {
        var genreDto = new GenreDto(1L, "Genre_1");
        var authorDto = new AuthorDto(1L, "Author_1");
        this.bookDto = new BookDto(1L, "BookTitle_1", authorDto, genreDto);
        this.expectedBookAsString = mapper.writeValueAsString(this.bookDto);
    }

    @Test
    @DisplayName("Возвращает книги в формате json")
    void shouldReturnExpectedBooksAsJson() throws Exception {
        given(bookService.findAll()).willReturn(List.of(this.bookDto));
        var expectedBooks = List.of(this.bookDto);
        mockMvc.perform(get("/api/v1/books"))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(expectedBooks)));

        verify(bookService, times(1)).findAll();
    }

    @Test
    @DisplayName("Возвращает книгу в формате json")
    void shouldReturnExpectedBookAsJson() throws Exception {
        given(bookService.findById(EXPECTED_BOOK_ID)).willReturn(Optional.of(this.bookDto));

        mockMvc.perform(get(String.format("/api/v1/books/%d", EXPECTED_BOOK_ID)))
                .andExpect(status().isOk())
                .andExpect(content().json(this.expectedBookAsString));

        verify(bookService, times(1)).findById(EXPECTED_BOOK_ID);
    }

    @Test
    @DisplayName("Создает книгу по данным из тела запроса, возвращает 201 статус")
    void shouldCreateBookWithExpectedResponseStatus() throws Exception {
        mockMvc.perform(post("/api/v1/books")
                        .contentType(APPLICATION_JSON)
                        .content(this.expectedBookAsString))
                .andExpect(status().isCreated());

        verify(bookService, times(1)).insert(
                this.bookDto.getTitle(),
                this.bookDto.getAuthor().getId(),
                this.bookDto.getGenre().getId()
        );
    }

    @Test
    @DisplayName("Изменяет книгу по данным из тела запроса")
    void shouldUpdateBookWithExpectedResponseStatus() throws Exception {
        this.bookDto.setTitle(EXPECTED_UPDATED_BOOK_TITLE);
        var expectedUpdatedBook = mapper.writeValueAsString(this.bookDto);
        given(bookService.update(
                this.bookDto.getId(),
                this.bookDto.getTitle(),
                this.bookDto.getAuthor().getId(),
                this.bookDto.getGenre().getId()
        )).willReturn(this.bookDto);
        mockMvc.perform(patch("/api/v1/books")
                        .contentType(APPLICATION_JSON)
                        .content(expectedUpdatedBook))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedUpdatedBook));

        verify(bookService, times(1)).update(
                this.bookDto.getId(),
                this.bookDto.getTitle(),
                this.bookDto.getAuthor().getId(),
                this.bookDto.getGenre().getId()
        );
    }

    @Test
    @DisplayName("Удаляет книгу, возвращает 204 статус")
    void shouldDeleteBookWithExpectedResponseStatus() throws Exception {
        mockMvc.perform(delete(String.format("/api/v1/books/%d", EXPECTED_BOOK_ID)))
                .andExpect(status().isNoContent());

        verify(bookService, times(1)).deleteById(EXPECTED_BOOK_ID);
    }

    @Test
    @DisplayName("Возвращает сообщения об ошибках при создании книги по некорректным данным")
    void shouldReturnErrorsMessagesOnInvalidPost() throws Exception {
        var invalidBook = new BookDto(0, "a", null, null);
        mockMvc.perform(
                        post("/api/v1/books")
                                .contentType(APPLICATION_JSON)
                                .content(mapper.writeValueAsString(invalidBook)))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(mapper.writeValueAsString(EXPECTED_ERROR_MESSAGES)));
    }

    @Test
    @DisplayName("Возвращает сообщения об ошибках при изменении книги с некорректными данными")
    void shouldReturnErrorsMessagesOnInvalidPatch() throws Exception {
        var invalidBook = new BookDto(1, "a", null, null);
        mockMvc.perform(
                        patch("/api/v1/books")
                                .contentType(APPLICATION_JSON)
                                .content(mapper.writeValueAsString(invalidBook)))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(mapper.writeValueAsString(EXPECTED_ERROR_MESSAGES)));
    }

    @Test
    @DisplayName("Возращает 500 статус если запрашиваемая книга не существует")
    void shouldReturnInternalServerErrorStatus() throws Exception {
        mockMvc.perform(get(String.format("/api/v1/books/%d", NON_EXISTENT_BOOK_ID)))
                .andExpect(status().isInternalServerError());
    }
}