package ru.otus.hw.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.models.Genre;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@DisplayName("Репозиторий на основе Jpa для работы с сущностями комментариев")
class JpaCommentRepositoryTest {
    private static final long EXPECTED_COMMENT_ID = 1L;

    private static final long EXPECTED_NEW_COMMENT_ID = 4L;

    private static final String EXPECTED_COMMENT_TEXT = "Comment_1";

    private static final String EXPECTED_UPDATED_COMMENT_TEXT = "New_Comment_1";

    private static final int EXPECTED_NUMBER_OF_COMMENTS = 1;

    private static final long EXPECTED_BOOK_ID = 1L;

    private static final long EXPECTED_GENRE_ID = 1L;

    private static final long EXPECTED_AUTHOR_ID = 1L;

    private Genre expectedGenre;

    private Author expectedAuthor;

    private Book expectedBook;

    private Comment expectedComment;

    @Autowired
    private TestEntityManager em;

    @Autowired
    private CommentRepository commentRepository;

    @BeforeEach
    void setUp() {
        expectedAuthor = em.find(Author.class, EXPECTED_AUTHOR_ID);
        expectedGenre = em.find(Genre.class, EXPECTED_GENRE_ID);
        expectedBook = em.find(Book.class, EXPECTED_BOOK_ID);
        expectedComment = new Comment(1, EXPECTED_COMMENT_TEXT, expectedBook);
    }

    @Test
    @DisplayName("Загружает ожидаемый комментарий по его идентификатору")
    void shouldFindExpectedCommentById() {
        var actualComment = commentRepository.findById(EXPECTED_COMMENT_ID);
        em.clear();

        assertThat(actualComment)
                .isPresent().get()
                .usingRecursiveComparison()
                .ignoringFields("book")
                .isEqualTo(expectedComment);
    }

    @Test
    @DisplayName("Загружает ожидаемый список комментариев по идентификатору книги")
    void shouldFindExpectedCommentByBookId() {
        var actualComments = commentRepository.findByBookId(EXPECTED_BOOK_ID);
        assertThat(actualComments).hasSize(EXPECTED_NUMBER_OF_COMMENTS).containsExactlyInAnyOrder(expectedComment);
    }

    @Test
    @DisplayName("Сохраняет комментарий")
    void shouldSaveComment() {
        var expectedComment = new Comment(EXPECTED_NEW_COMMENT_ID, EXPECTED_COMMENT_TEXT, expectedBook);
        commentRepository.save(new Comment(0, EXPECTED_COMMENT_TEXT, expectedBook));
        em.flush();
        em.clear();

        assertThat(em.find(Comment.class, EXPECTED_NEW_COMMENT_ID))
                .isNotNull()
                .usingRecursiveComparison().ignoringFields("book").isEqualTo(expectedComment);
    }

    @Test
    @DisplayName("Обновляет комментарий")
    void shouldUpdateComment() {
        var comment = expectedComment.copy();
        comment.setText(EXPECTED_UPDATED_COMMENT_TEXT);
        commentRepository.save(comment);
        em.flush();
        em.clear();
        var updatedComment = em.find(Comment.class, EXPECTED_COMMENT_ID);
        assertThat(updatedComment).isNotNull().usingRecursiveComparison().ignoringFields("book").isEqualTo(comment);
    }

    @Test
    @DisplayName("Удаляет комментарий по идентификатору")
    void shouldDeleteById() {
        commentRepository.deleteById(EXPECTED_COMMENT_ID);
        em.flush();
        em.detach(expectedComment);
        assertThat(em.find(Comment.class, EXPECTED_COMMENT_ID)).isNull();
    }
}