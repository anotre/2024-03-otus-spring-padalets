package ru.otus.hw.controllers;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.otus.hw.config.security.SecurityConfiguration;
import ru.otus.hw.controllers.dto.BookDto;
import ru.otus.hw.services.AuthorService;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.CommentService;
import ru.otus.hw.services.GenreService;

import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Stream;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = {BookController.class})
@Import({SecurityConfiguration.class})
class BookControllerSecurityTest {
    private BookDto bookDto;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    @MockBean
    private AuthorService authorService;

    @MockBean
    private GenreService genreService;

    @MockBean
    private CommentService commentService;

    @ParameterizedTest(name = "{0} {1} for user {2} should return {4} status")
    @MethodSource("getTestData")
    void shouldReturnExpectedStatus(
            String method,
            String url,
            String user,
            GrantedAuthority[] authorities,
            int responseStatus,
            String redirectUrlPattern
    ) throws Exception {
        var request = this.methodToRequestBuilder(method, url);

        if (Objects.nonNull(user)) {
            request.with(user(user).authorities(authorities));
        }

        ResultActions resultActions = mockMvc.perform(request)
                .andExpect(status().is(responseStatus));

        if (!redirectUrlPattern.isEmpty()) {
            resultActions.andExpect(redirectedUrlPattern(redirectUrlPattern));
        }
    }

    private MockHttpServletRequestBuilder methodToRequestBuilder(String method, String url) {
        Map<String, Function<String, MockHttpServletRequestBuilder>> methodMap = Map.of(
                "get", MockMvcRequestBuilders::get,
                "post", MockMvcRequestBuilders::post
        );

        return methodMap.get(method).apply(url);
    }

    public static Stream<Arguments> getTestData() {
        var user = "user1";
        var authorities = new GrantedAuthority[]{
                new SimpleGrantedAuthority("ROLE_ADMIN")
        };
        return Stream.of(
                Arguments.of("get", "/", user, authorities, 200, ""),
                Arguments.of("get", "/", null, null, 302, "**/login"),
                Arguments.of("get", "/books", user, authorities, 200, ""),
                Arguments.of("get", "/books", null, null, 302, "**/login"),
                Arguments.of("get", "/books/1", user, authorities, 404, ""),
                Arguments.of("get", "/books/1", null, null, 302, "**/login"),
                Arguments.of("get", "/books/create", user, authorities, 200, ""),
                Arguments.of("get", "/books/create", null, null, 302, "**/login"),
                Arguments.of("get", "/books/edit/1", user, authorities, 404, ""),
                Arguments.of("get", "/books/edit/1", null, null, 302, "**/login"),

                Arguments.of("post", "/books/create", user, authorities, 200, ""),
                Arguments.of("post", "/books/create", null, null, 302, "**/login"),
                Arguments.of("post", "/books/edit", user, authorities, 200, ""),
                Arguments.of("post", "/books/edit", null, null, 302, "**/login"),
                Arguments.of("post", "/books/delete/1", user, authorities, 302, "/{books}"),
                Arguments.of("post", "/books/delete/1", null, null, 302, "**/login")
        );
    }
}