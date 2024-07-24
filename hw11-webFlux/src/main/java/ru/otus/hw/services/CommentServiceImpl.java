package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.controllers.dto.CommentDto;
import ru.otus.hw.controllers.dto.converter.CommentDtoConverter;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Comment;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.CommentRepository;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;

    private final BookRepository bookRepository;

    private final CommentDtoConverter converter;

    @Override
    public Mono<CommentDto> findById(String id) {
        return commentRepository.findById(id).map(converter::toDto);
    }

    @Override
    public Flux<CommentDto> findByBookId(String id) {
        return commentRepository.findByBookId(id).map(converter::toDto);
    }

    @Override
    public Mono<CommentDto> insert(String text, String bookId) {
        return this.prepareComment(text, bookId)
                .flatMap(commentRepository::save)
                .map(converter::toDto);
    }

    @Override
    public Mono<CommentDto> update(String id, String text, String bookId) {
        return this.prepareComment(text, bookId)
                .flatMap(comment -> {
                    comment.setId(id);
                    return commentRepository.save(comment);
                })
                .map(converter::toDto);
    }

    @Override
    public void deleteById(String id) {
        commentRepository.deleteById(id).subscribe();
    }

    private Mono<Comment> prepareComment(String text, String bookId) {
        return bookRepository.findById(bookId)
                .switchIfEmpty(
                        Mono.error(new EntityNotFoundException(
                                String.format("Book with id %s not found".formatted(bookId))
                        ))
                )
                .flatMap(book -> commentRepository.save(new Comment(text, book)));
    }
}
