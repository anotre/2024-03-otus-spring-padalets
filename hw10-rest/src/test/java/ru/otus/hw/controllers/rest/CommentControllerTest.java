package ru.otus.hw.controllers.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.controllers.dto.CommentDto;
import ru.otus.hw.services.CommentService;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CommentController.class)
@DisplayName("REST controller for comments")
@Import({CommentController.class})
class CommentControllerTest {
    private static final long BOOK_ID = 1L;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private CommentService commentService;

    @Test
    @DisplayName("Возвращает все комментарии по переданному идентификатору книги")
    void getAllCommentsByBookId() throws Exception {
        var expectedComments = List.of(
                new CommentDto(1L, "Comment_1", null),
                new CommentDto(2L, "Comment_1", null)
        );
        given(commentService.findByBookId(BOOK_ID)).willReturn(expectedComments);
        mockMvc.perform(get("/api/v1/comments").param("bookId", String.valueOf(BOOK_ID)))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(expectedComments)));

        verify(commentService, times(1)).findByBookId(BOOK_ID);
    }
}