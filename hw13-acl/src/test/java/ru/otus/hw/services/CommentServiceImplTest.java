package ru.otus.hw.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.controllers.dto.AuthorDto;
import ru.otus.hw.controllers.dto.BookDto;
import ru.otus.hw.controllers.dto.CommentDto;
import ru.otus.hw.controllers.dto.GenreDto;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@DisplayName("Сервис для работы с сущностями комментариев")
@ComponentScan(value = {"ru.otus.hw.repositories", "ru.otus.hw.controllers.dto"})
@Import({CommentServiceImpl.class})
@Transactional(propagation = Propagation.NEVER)
class CommentServiceImplTest {
    private static final long EXPECTED_COMMENT_ID = 1L;

    private static final long EXPECTED_NEW_COMMENT_ID = 4L;

    private static final String EXPECTED_COMMENT_TEXT = "Comment_1";

    private static final String EXPECTED_UPDATED_COMMENT_TEXT = "New_Comment_1";

    private static final int EXPECTED_NUMBER_OF_COMMENTS_BY_BOOK_ID = 1;

    private static final long EXPECTED_BOOK_ID = 1L;

    private static final String EXPECTED_BOOK_TITLE = "BookTitle_1";

    private static final long EXPECTED_AUTHOR_ID = 1L;

    private static final String EXPECTED_AUTHOR_FULL_NAME = "Author_1";

    private static final long EXPECTED_GENRE_ID = 1L;

    private static final String EXPECTED_GENRE_NAME = "Genre_1";

    private GenreDto expectedGenreDto;

    private AuthorDto expectedAuthorDto;

    private BookDto expectedBookDto;

    private CommentDto expectedCommentDto;

    @Autowired
    private CommentService commentService;

    @BeforeEach
    void setUp() {
        expectedAuthorDto = new AuthorDto(EXPECTED_AUTHOR_ID, EXPECTED_AUTHOR_FULL_NAME);

        expectedGenreDto = new GenreDto(EXPECTED_GENRE_ID, EXPECTED_GENRE_NAME);
        expectedBookDto = new BookDto(EXPECTED_BOOK_ID, EXPECTED_BOOK_TITLE, expectedAuthorDto, expectedGenreDto);
        expectedCommentDto = new CommentDto(0, EXPECTED_COMMENT_TEXT, expectedBookDto);
    }

    @Test
    @DisplayName("Загружает ожидаемый комментарий по идентификатору")
    void shouldFindExpectedCommentById() {
        var actualCommentDto = commentService.findById(EXPECTED_COMMENT_ID);
        expectedCommentDto.setId(EXPECTED_COMMENT_ID);
        assertThat(actualCommentDto)
                .isPresent()
                .get().usingRecursiveComparison()
                .ignoringFields("book")
                .isEqualTo(expectedCommentDto);
    }

    @Test
    @DisplayName("Загружает список комментариев по идентификатору книги")
    void shouldFindExpectedCommentListByBookId() {
        var actualCommentsDto = commentService.findByBookId(EXPECTED_BOOK_ID);
        expectedCommentDto.setId(EXPECTED_COMMENT_ID);
        assertThat(actualCommentsDto)
                .hasSize(EXPECTED_NUMBER_OF_COMMENTS_BY_BOOK_ID)
                .containsExactlyInAnyOrder(expectedCommentDto);
    }

    @Test
    @DisplayName("Сохраняет новый комментарий")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void shouldCreateComment() {
        var newCommentDto = commentService.insert(EXPECTED_COMMENT_TEXT, EXPECTED_BOOK_ID);
        expectedCommentDto.setId(EXPECTED_NEW_COMMENT_ID);
        assertThat(newCommentDto).usingRecursiveComparison().isEqualTo(expectedCommentDto);
    }

    @Test
    @DisplayName("Обновляет существующий комментарий")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void shouldChangeExistingComment() {
        var updatedCommentDto = commentService.update(EXPECTED_COMMENT_ID, EXPECTED_UPDATED_COMMENT_TEXT, EXPECTED_BOOK_ID);
        expectedCommentDto.setId(EXPECTED_COMMENT_ID);
        expectedCommentDto.setText(EXPECTED_UPDATED_COMMENT_TEXT);
        assertThat(updatedCommentDto).usingRecursiveComparison().isEqualTo(expectedCommentDto);
    }

    @Test
    @DisplayName("Удаляет комментарий по идентификатору")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void shouldDeleteCommentById() {
        assertThat(commentService.findById(EXPECTED_COMMENT_ID)).isPresent();
        commentService.deleteById(EXPECTED_COMMENT_ID);
        assertThat(commentService.findById(EXPECTED_COMMENT_ID)).isNotPresent();
    }
}