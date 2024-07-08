package ru.otus.hw.controllers.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.controllers.dto.AuthorDto;
import ru.otus.hw.services.AuthorService;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthorController.class)
@Import({AuthorController.class})
@DisplayName("REST controller for authors")
class AuthorControllerTest {
    public static final List<AuthorDto> EXPECTED_AUTHORS = List.of(
            new AuthorDto(1, "Author_1"),
            new AuthorDto(1, "Author_2")
    );

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private AuthorService authorService;

    @Test
    @DisplayName("Возвращает всех авторов в форме Json")
    void shouldReturnExpectedAuthorsAsJson() throws Exception {
        given(authorService.findAll()).willReturn(EXPECTED_AUTHORS);

        mockMvc.perform(get("/api/v1/authors"))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(EXPECTED_AUTHORS)));

        verify(authorService, times(1)).findAll();
    }
}