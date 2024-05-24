package ru.otus.hw.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.models.Genre;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@DisplayName("Репозиторий на основе Jpa для работы с сущностями комментариев")
@Import(JpaCommentRepository.class)
class JpaCommentRepositoryTest {
    private static final long EXPECTED_COMMENT_ID = 1L;

    private static final long EXPECTED_NEW_COMMENT_ID = 4L;

    private static final String EXPECTED_COMMENT_TEXT = "new text";

    private static final int EXPECTED_NUMBER_OF_COMMENTS = 1;

    private static final long EXPECTED_BOOK_ID = 1L;

    private static final String EXPECTED_BOOK_TITLE = "BookTitle_1";

    private static final long EXPECTED_GENRE_ID = 1L;

    private static final String EXPECTED_GENRE_NAME = "Genre_1";

    private static final long EXPECTED_AUTHOR_ID = 1L;

    private static final String EXPECTED_AUTHOR_FULL_NAME = "Author_1";

    @Autowired
    private TestEntityManager em;

    @Autowired
    private CommentRepository commentRepository;

    @Test
    @DisplayName("Загружает ожидаемый комментарий по его идентификатору")
    void shouldFindExpectedCommentById() {
        var actualComment = commentRepository.findById(EXPECTED_COMMENT_ID);
        em.clear();
        var expectedComment = em.find(Comment.class, EXPECTED_COMMENT_ID);
        assertThat(actualComment).isPresent().get().isEqualTo(expectedComment);
    }

    @Test
    @DisplayName("Загружает ожидаемый список комментариев по идентификатору книги")
    void shouldFindExpectedCommentByBookId() {
        var actualComments = commentRepository.findByBookId(EXPECTED_BOOK_ID);
        em.clear();
        var expectedComment = em.find(Comment.class, EXPECTED_COMMENT_ID);
        assertThat(actualComments).hasSize(EXPECTED_NUMBER_OF_COMMENTS).containsExactlyInAnyOrder(expectedComment);
    }

    @Test
    @DisplayName("Сохраняет комментарий")
    void shouldSaveComment() {
        var author = new Author(EXPECTED_AUTHOR_ID, EXPECTED_AUTHOR_FULL_NAME);
        var genre = new Genre(EXPECTED_GENRE_ID, EXPECTED_GENRE_NAME);
        var book = new Book(EXPECTED_BOOK_ID, EXPECTED_BOOK_TITLE, author, genre);
        var expectedComment = new Comment(EXPECTED_NEW_COMMENT_ID, EXPECTED_COMMENT_TEXT, book);
        var newComment = commentRepository.save(new Comment(0, EXPECTED_COMMENT_TEXT, book));
        assertThat(newComment).isNotNull().usingRecursiveComparison().isEqualTo(expectedComment);
    }

    @Test
    @DisplayName("Обновляет комментарий")
    void shouldUpdateComment() {
        var oldComment = em.find(Comment.class, EXPECTED_COMMENT_ID);
        var oldText = oldComment.getText();
        oldComment.setText(EXPECTED_COMMENT_TEXT);
        em.detach(oldComment);
        commentRepository.update(oldComment);
        var newComment = em.find(Comment.class, EXPECTED_COMMENT_ID);
        assertThat(newComment.getText()).isNotEqualTo(oldText).isEqualTo(EXPECTED_COMMENT_TEXT);
    }

    @Test
    @DisplayName("Удаляет комментарий по идентификатору")
    void shouldDeleteById() {
        var comment = em.find(Comment.class, EXPECTED_COMMENT_ID);
        assertThat(comment).isNotNull();
        commentRepository.deleteById(EXPECTED_COMMENT_ID);
        assertThat(em.find(Comment.class, EXPECTED_COMMENT_ID)).isNull();
    }
}