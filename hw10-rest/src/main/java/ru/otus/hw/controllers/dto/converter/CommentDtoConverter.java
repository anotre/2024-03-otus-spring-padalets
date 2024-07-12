package ru.otus.hw.controllers.dto.converter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.hw.dto.converter.DtoConverter;
import ru.otus.hw.models.Comment;
import ru.otus.hw.controllers.dto.CommentDto;

@Component
@RequiredArgsConstructor
public class CommentDtoConverter implements DtoConverter<Comment, CommentDto> {
    private final BookDtoConverter bookDtoConverter;

    @Override
    public CommentDto toDto(Comment comment) {
        var bookDto = bookDtoConverter.toDto(comment.getBook());

        return new CommentDto(
                comment.getId(),
                comment.getText(),
                bookDto);
    }

    @Override
    public Comment toDomain(CommentDto commentDto) {
        var book = bookDtoConverter.toDomain(commentDto.getBook());

        return new Comment(
                commentDto.getId(),
                commentDto.getText(),
                book);
    }
}
