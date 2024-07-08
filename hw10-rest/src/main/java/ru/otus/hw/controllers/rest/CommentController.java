package ru.otus.hw.controllers.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.hw.controllers.dto.CommentDto;
import ru.otus.hw.services.CommentService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @GetMapping(value = "/api/v1/comments", params = {"bookId"})
    public List<CommentDto> getAllCommentsByBookId(@RequestParam long bookId) {
        return commentService.findByBookId(bookId);
    }
}
