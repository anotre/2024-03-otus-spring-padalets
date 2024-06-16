package ru.otus.hw.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.annotation.DirtiesContext;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.models.Genre;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
@DisplayName("Сервис для работы с сущностями комментариев")
@ComponentScan({"ru.otus.hw.repositories", "ru.otus.hw.services", "ru.otus.hw.events"})
class CommentServiceImplTest {
    private static final String EXPECTED_COMMENT_ID = "1";

    private static final String EXPECTED_COMMENT_TEXT = "Comment_1";

    private static final String EXPECTED_UPDATED_COMMENT_TEXT = "New_Comment_1";

    private static final int EXPECTED_NUMBER_OF_COMMENTS_BY_BOOK_ID = 1;

    private static final String EXPECTED_BOOK_ID = "1";

    private static final String EXPECTED_BOOK_TITLE = "BookTitle_1";

    private static final String EXPECTED_AUTHOR_ID = "1";

    private static final String EXPECTED_AUTHOR_FULL_NAME = "Author_1";

    private static final String EXPECTED_GENRE_ID = "1";

    private static final String EXPECTED_GENRE_NAME = "Genre_1";

    private Genre expectedGenre;

    private Author expectedAuthor;

    private Book expectedBook;

    private Comment expectedComment;

    @Autowired
    private CommentService commentService;

    @BeforeEach
    void setUp() {
        expectedAuthor = new Author(EXPECTED_AUTHOR_ID, EXPECTED_AUTHOR_FULL_NAME);
        expectedGenre = new Genre(EXPECTED_GENRE_ID, EXPECTED_GENRE_NAME);
        expectedBook = new Book(EXPECTED_BOOK_ID, EXPECTED_BOOK_TITLE, expectedAuthor, expectedGenre);
        expectedComment = new Comment(EXPECTED_COMMENT_TEXT, expectedBook);
    }

    @Test
    @DisplayName("Загружает ожидаемый комментарий по идентификатору")
    void shouldFindExpectedCommentById() {
        var actualComment = commentService.findById(EXPECTED_COMMENT_ID);
        expectedComment.setId(EXPECTED_COMMENT_ID);
        assertThat(actualComment)
                .isPresent()
                .get().usingRecursiveComparison()
                .ignoringFields("book")
                .isEqualTo(expectedComment);
    }

    @Test
    @DisplayName("Загружает список комментариев по идентификатору книги")
    void shouldFindExpectedCommentListByBookId() {
        var actualComments = commentService.findByBookId(EXPECTED_BOOK_ID);
        expectedComment.setId(EXPECTED_COMMENT_ID);
        assertThat(actualComments)
                .hasSize(EXPECTED_NUMBER_OF_COMMENTS_BY_BOOK_ID)
                .containsExactlyInAnyOrder(expectedComment);
    }

    @Test
    @DisplayName("Сохраняет новый комментарий")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void shouldCreateComment() {
        var newComment = commentService.insert(EXPECTED_COMMENT_TEXT, EXPECTED_BOOK_ID);
        var expectedComment = commentService.findById(newComment.getId());
        assertThat(expectedComment).isPresent();
        assertThat(newComment).usingRecursiveComparison().isEqualTo(expectedComment.get());
    }

    @Test
    @DisplayName("Обновляет существующий комментарий")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void shouldChangeExistingComment() {
        var updatedComment = commentService.update(EXPECTED_COMMENT_ID, EXPECTED_UPDATED_COMMENT_TEXT, EXPECTED_BOOK_ID);
        expectedComment.setId(EXPECTED_COMMENT_ID);
        expectedComment.setText(EXPECTED_UPDATED_COMMENT_TEXT);
        assertThat(updatedComment).usingRecursiveComparison().isEqualTo(expectedComment);
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