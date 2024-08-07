package ru.otus.hw.controllers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockBeans;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.config.security.SecurityConfiguration;
import ru.otus.hw.controllers.dto.AuthorDto;
import ru.otus.hw.controllers.dto.BookDto;
import ru.otus.hw.controllers.dto.GenreDto;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.CommentRepository;
import ru.otus.hw.repositories.GenreRepository;
import ru.otus.hw.repositories.security.UserDetailsRepository;
import ru.otus.hw.services.AuthorService;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.GenreService;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
@ContextConfiguration(classes = BookController.class)
@Import({SecurityConfiguration.class})
@MockBeans({
        @MockBean(CommentRepository.class),
        @MockBean(BookRepository.class),
        @MockBean(AuthorRepository.class),
        @MockBean(GenreRepository.class),
        @MockBean(UserDetailsRepository.class)
})
@ComponentScan({
        "ru.otus.hw.repositories",
        "ru.otus.hw.services",
        "ru.otus.hw.controllers.dto.converter"
})
class BookControllerSecurityTest {
    public final static long EXPECTED_BOOK_ID = 1;

    public final static String USER = "user1";

    public final static String AUTHORITY = "ROLE_ADMIN";

    private BookDto bookDto;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    @MockBean
    private AuthorService authorService;

    @MockBean
    private GenreService genreService;

    @ParameterizedTest
    @CsvSource(value = {
            "/",
            "/books",
            "/books",
            "/books",
    })
    @DisplayName("Для доступа требуется аутентификация")
    void mustBeAuthenticatedToGetResources(String url) throws Exception {
        var genreDto = new GenreDto(1L, "Genre_1");
        var authorDto = new AuthorDto(1L, "Author_1");
        this.bookDto = new BookDto(1L, "BookTitle_1", authorDto, genreDto);

        given(bookService.findById(EXPECTED_BOOK_ID)).willReturn(Optional.of(bookDto));
        given(authorService.findAll()).willReturn(Collections.emptyList());
        given(genreService.findAll()).willReturn(Collections.emptyList());

        mockMvc.perform(get(url)
                        .with(user(USER).authorities(new SimpleGrantedAuthority(AUTHORITY))))
                .andExpect(status().isOk());

        mockMvc.perform(get(url))
                .andExpect(status().is3xxRedirection());
    }

    @ParameterizedTest
    @CsvSource(value = {
            "/books/create",
            "/books/edit",
    })
    @DisplayName("Для изменения данных требуется аутентификация")
    void mustBeAuthenticatedToChangeResources(String url) throws Exception {
        var testTitle = "test title";
        var testId = "1";

        mockMvc.perform(post(url)
                        .param("title", testTitle)
                        .param("author.id", testId)
                        .param("genre.id", testId)
                        .with(user(USER).authorities(new SimpleGrantedAuthority(AUTHORITY)))
                ).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/books"));

        mockMvc.perform(post(url)
                        .param("title", testTitle)
                        .param("author.id", testId)
                        .param("genre.id", testId)
                ).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
    }

    @Test
    @DisplayName("Для удаления требуется аутентификация")
    void mustBeAuthenticatedToDeleteResources() throws Exception {
        var deleteUrl = "/books/delete/1";
        mockMvc.perform(post(deleteUrl)
                        .with(user(USER).authorities(new SimpleGrantedAuthority(AUTHORITY)))
                ).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/books"));

        mockMvc.perform(post(deleteUrl))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
    }
}