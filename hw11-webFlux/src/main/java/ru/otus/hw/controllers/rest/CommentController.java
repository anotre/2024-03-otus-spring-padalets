package ru.otus.hw.controllers.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Scheduler;
import ru.otus.hw.controllers.dto.CommentDto;
import ru.otus.hw.services.CommentService;

@RestController
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    private final Scheduler workerPool;
    
    @GetMapping("/api/v1/comments/book/{bookId}")
    public Flux<CommentDto> getAllCommentsByBookId(@PathVariable String bookId) {
        return commentService.findByBookId(bookId).subscribeOn(workerPool);
    }
}
