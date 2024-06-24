package ru.otus.hw.rest.dto.converter;

import org.springframework.stereotype.Component;
import ru.otus.hw.models.Comment;
import ru.otus.hw.rest.dto.CommentDto;

@Component
public class CommentDtoConverter {
    public CommentDto convert(Comment comment) {
        return new CommentDto(
                comment.getId(),
                comment.getText(),
                comment.getBook().getId());
    }
}
