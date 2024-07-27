package ru.otus.hw.services;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import ru.otus.hw.controllers.dto.CommentDto;
import ru.otus.hw.controllers.dto.converter.CommentDtoConverter;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.CommentRepository;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@DisplayName("Сервис для работы с сущностями комментариев")
@Import({CommentServiceImpl.class})
class CommentServiceImplTest {
    private static final String EXPECTED_COMMENT_ID = "testCommentId";

    private static final String EXPECTED_COMMENT_TEXT = "Comment_1";

    private static final String EXPECTED_UPDATED_COMMENT_TEXT = "New_Comment_1";

    private static final String EXPECTED_BOOK_ID = "testBookId";

    private CommentDto expectedComment;

    @Autowired
    private CommentService commentService;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private CommentDtoConverter commentDtoConverter;

    @Test
    @DisplayName("Загружает ожидаемый комментарий по идентификатору")
    void shouldFindExpectedCommentById() {

        var expectedComment = commentRepository
                .findById(EXPECTED_COMMENT_ID)
                .map(commentDtoConverter::toDto);

        var actualComment = commentService.findById(EXPECTED_COMMENT_ID);

        StepVerifier.create(Flux.zip(actualComment, expectedComment))
                .assertNext(
                        tuple -> Assertions.assertThat(tuple.getT1()).isEqualTo(tuple.getT2())
                )
                .expectComplete()
                .verify();
    }

    @Test
    @DisplayName("Загружает список комментариев по идентификатору книги")
    void shouldFindExpectedCommentListByBookId() {
        var expectedComments = commentRepository
                .findByBookId(EXPECTED_BOOK_ID)
                .map(commentDtoConverter::toDto);
        var actualComments = commentService.findByBookId(EXPECTED_BOOK_ID);

        StepVerifier
                .create(Flux.zip(actualComments, expectedComments))
                .assertNext(
                        tuple -> {
                            assertThat(tuple.getT1()).isEqualTo(tuple.getT2());
                        }
                ).expectComplete()
                .verify();
    }

    @Test
    @DisplayName("Сохраняет новый комментарий")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void shouldCreateComment() {
        Mono<CommentDto> newComment = bookRepository.findById(EXPECTED_BOOK_ID)
                .flatMap(book -> commentService.insert(EXPECTED_COMMENT_TEXT, book.getId()));

        StepVerifier.create(newComment)
                .assertNext(comment -> {
                    assertThat(comment.getId()).isNotNull();
                    assertThat(comment.getText()).isEqualTo(EXPECTED_COMMENT_TEXT);
                }).expectComplete().verify();
    }

    @Test
    @DisplayName("Обновляет существующий комментарий")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void shouldChangeExistingComment() {
        var updatedComment = commentService
                .update(EXPECTED_COMMENT_ID, EXPECTED_UPDATED_COMMENT_TEXT, EXPECTED_BOOK_ID)
                .flatMap(comment -> commentRepository.findById(EXPECTED_COMMENT_ID));

        StepVerifier.create(updatedComment)
                .assertNext(comment -> assertThat(comment.getText()).isEqualTo(EXPECTED_UPDATED_COMMENT_TEXT))
                .expectComplete()
                .verify();
    }

    @Test
    @DisplayName("Удаляет комментарий по идентификатору")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void shouldDeleteCommentById() {
        var expectedEmptyComment = commentRepository
                .findById(EXPECTED_COMMENT_ID)
                .flatMap(comment -> commentService
                        .deleteById(comment.getId())
                        .thenReturn(comment))
                .flatMap(comment -> commentRepository.findById(comment.getId()));

        StepVerifier
                .create(expectedEmptyComment)
                .expectNextCount(0)
                .expectComplete()
                .verify();
    }
}