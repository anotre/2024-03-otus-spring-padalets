package ru.otus.hw.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.controllers.dto.AuthorDto;
import ru.otus.hw.controllers.dto.BookDto;
import ru.otus.hw.controllers.dto.GenreDto;
import ru.otus.hw.controllers.exceptions.NotFoundException;
import ru.otus.hw.services.AuthorService;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.CommentService;
import ru.otus.hw.services.GenreService;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@Import({BookController.class})
@WebMvcTest(BookController.class)
@DisplayName("MVC controller for books")
class BookControllerTest {
    public static final long EXPECTED_BOOK_ID = 1L;

    public static final String EXPECTED_UPDATED_BOOK_TITLE = "updated_book_title";

    public static final String BOOKS_LIST_URL = "/books";

    public static final String EDIT_URL = "/books/edit";

    public static final String CREATE_URL = "/books/create";

    public static final String DELETE_URL = "/books/delete";

    public static final String NOT_FOUND_VIEW = "not-found";

    public static final String CREATE_VIEW = "create-book";

    public static final String EDIT_VIEW = "edit-book";

    public static final String BOOKS_LIST_VIEW = "books-list";

    public static final String BOOK_VIEW = "book";

    private BookDto bookDto;

    @MockBean
    private BookService bookService;

    @MockBean
    private CommentService commentService;

    @MockBean
    private AuthorService authorService;

    @MockBean
    private GenreService genreService;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        var genreDto = new GenreDto(1L, "Genre_1");
        var authorDto = new AuthorDto(1L, "Author_1");
        this.bookDto = new BookDto(1L, "BookTitle_1", authorDto, genreDto);
    }

    @Test
    @DisplayName("Возвращает страницу с книгами")
    void shouldReturnBooksListViewWithExpectedBooks() throws Exception {
        var expectedBooks = List.of(bookDto);
        given(bookService.findAll()).willReturn(expectedBooks);

        mockMvc.perform(get(BOOKS_LIST_URL))
                .andExpect(status().isOk())
                .andExpect(view().name(BOOKS_LIST_VIEW))
                .andExpect(model().attributeExists("books"))
                .andExpect(model().attribute("books", expectedBooks));
    }

    @Test
    @DisplayName("Возвращает страницу с данными книги и ее комментариями")
    void shouldReturnBookViewWithExpectedBookData() throws Exception {
        given(bookService.findById(bookDto.getId())).willReturn(Optional.of(bookDto));
        given(commentService.findByBookId(bookDto.getId())).willReturn(Collections.emptyList());

        mockMvc.perform(get(String.format("%s/%d", BOOKS_LIST_URL, bookDto.getId())))
                .andExpect(status().isOk())
                .andExpect(view().name(BOOK_VIEW))
                .andExpect(model().attributeExists("bookDto", "comments"))
                .andExpect(model().attribute("bookDto", bookDto));

        verify(bookService, times(1)).findById(bookDto.getId());
        verify(commentService, times(1)).findByBookId(bookDto.getId());
    }

    @Test
    @DisplayName("Возвращает страницу создания с загруженными опциями для заполнения формы")
    void shouldReturnCreateViewWithExistentBookDataWithFormOptions() throws Exception {
        given(authorService.findAll()).willReturn(Collections.emptyList());
        given(genreService.findAll()).willReturn(Collections.emptyList());

        mockMvc.perform(get(CREATE_URL))
                .andExpect(status().isOk())
                .andExpect(view().name(CREATE_VIEW))
                .andExpect(model().attributeExists("authors", "genres"));
    }

    @Test
    @DisplayName("Возвращает страницу редактирования с данными существующей книги и загруженными опциями для заполнения формы")
    void shouldReturnEditViewWithExistentBookDataWithFormOptions() throws Exception {
        given(bookService.findById(bookDto.getId())).willReturn(Optional.of(bookDto));
        given(authorService.findAll()).willReturn(Collections.emptyList());
        given(genreService.findAll()).willReturn(Collections.emptyList());

        mockMvc.perform(get(String.format("%s/%d", EDIT_URL, bookDto.getId())))
                .andExpect(status().isOk())
                .andExpect(view().name(EDIT_VIEW))
                .andExpect(model().attributeExists("authors", "genres", "bookDto"))
                .andExpect(model().attribute("bookDto", bookDto));

        verify(bookService, times(1)).findById(bookDto.getId());
    }

    @Test
    @DisplayName("Создает книгу и перенаправляет на страницу списка книг")
    void shouldCreateBookAndRedirect() throws Exception {
        mockMvc.perform(post(CREATE_URL)
                        .param("title", bookDto.getTitle())
                        .param("author.id", String.valueOf(bookDto.getAuthor().getId()))
                        .param("genre.id", String.valueOf(bookDto.getGenre().getId())))
                .andExpect(status().is3xxRedirection())
                .andExpect(model().hasNoErrors())
                .andExpect(redirectedUrl(BOOKS_LIST_URL));

        verify(bookService, times(1)).insert(
                bookDto.getTitle(),
                bookDto.getAuthor().getId(),
                bookDto.getGenre().getId());
    }

    @Test
    @DisplayName("Возвращает обратно на форму создания если в ней обнаружены ошибки")
    void shouldReturnCreateFormOnSendBadForm() throws Exception {
        var badTitle = "A";
        var absentId = 0;
        GenreDto badGenre = null;
        AuthorDto badAuthor = null;

        mockMvc.perform(post(CREATE_URL)
                        .param("title", badTitle)
                        .param("author.id", String.valueOf(badAuthor))
                        .param("genre.id", String.valueOf(badGenre)))
                .andExpect(model().errorCount(3))
                .andExpect(view().name(CREATE_VIEW));

        verify(bookService, times(0)).insert(
                badTitle,
                absentId,
                absentId);
    }

    @Test
    @DisplayName("Обновляет книгу и перенаправляет на страницу списка книг")
    void shouldUpdateBookAndRedirect() throws Exception {
        var updatedBook = new BookDto(
                bookDto.getId(),
                EXPECTED_UPDATED_BOOK_TITLE,
                bookDto.getAuthor(),
                bookDto.getGenre()
        );

        mockMvc.perform(post(EDIT_URL)
                        .param("id", String.valueOf(updatedBook.getId()))
                        .param("title", updatedBook.getTitle())
                        .param("author.id", String.valueOf(updatedBook.getAuthor().getId()))
                        .param("genre.id", String.valueOf(updatedBook.getGenre().getId())))
                .andExpect(model().hasNoErrors())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(BOOKS_LIST_URL));

        verify(bookService, times(1)).update(
                updatedBook.getId(),
                updatedBook.getTitle(),
                updatedBook.getAuthor().getId(),
                updatedBook.getGenre().getId());
    }

    @Test
    @DisplayName("Возвращает обратно на форму редактирования если в ней обнаружены ошибки")
    void shouldReturnEditFormOnSendBadForm() throws Exception {
        var badTitle = "A";
        var absentId = 0;
        GenreDto badGenre = null;
        AuthorDto badAuthor = null;

        mockMvc.perform(post(EDIT_URL)
                        .param("id", String.valueOf(bookDto.getId()))
                        .param("title", badTitle)
                        .param("author.id", String.valueOf(badAuthor))
                        .param("genre.id", String.valueOf(badGenre)))
                .andExpect(view().name(EDIT_VIEW))
                .andExpect(model().errorCount(3));

        verify(bookService, times(0)).update(
                bookDto.getId(),
                badTitle,
                absentId,
                absentId);
    }

    @Test
    @DisplayName("Удаляет книгу и перенаправляет на список всех книг")
    void shouldDeleteById() throws Exception {
        mockMvc.perform(post(String.format("%s/%d", DELETE_URL, EXPECTED_BOOK_ID)))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(BOOKS_LIST_URL));

        verify(bookService, times(1)).deleteById(EXPECTED_BOOK_ID);
    }

    @Test
    @DisplayName("Перенаправляет на страницу \"Не найдено\", если запрашиваемой книги нет")
    void shouldReturnExpectedStatusAndViewWhenThrowsNotFound() throws Exception {
        given(bookService.findById(EXPECTED_BOOK_ID)).willThrow(NotFoundException.class);
        mockMvc.perform(
                        get(String.format("%s/%d", BOOKS_LIST_URL, EXPECTED_BOOK_ID)))
                .andExpect(view().name(NOT_FOUND_VIEW))
                .andExpect(status().isNotFound());
    }
}