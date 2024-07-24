package ru.otus.hw.controllers.rest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.support.WebExchangeBindException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import ru.otus.hw.controllers.dto.BookDto;
import ru.otus.hw.controllers.exceptions.NotFoundException;
import ru.otus.hw.services.BookService;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class BookController {
    private final BookService bookService;

    private final Scheduler workerPool;

    @GetMapping("/api/v1/books")
    public Flux<BookDto> getAllBooks() {
        return bookService.findAll().subscribeOn(workerPool);
    }

    @GetMapping("/api/v1/books/{id}")
    public Mono<BookDto> getBook(@PathVariable String id) {
        return bookService.findById(id)
                .switchIfEmpty(Mono.error(new NotFoundException())).subscribeOn(workerPool);
    }

    @PostMapping("/api/v1/books")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Void> createBook(@Valid @RequestBody BookDto bookDto) {
        return Mono.just(bookDto).subscribeOn(workerPool).flatMap(bookDtoItem -> bookService.insert(
                        bookDtoItem.getTitle(),
                        bookDtoItem.getAuthor().getId(),
                        bookDtoItem.getGenre().getId()))
                .flatMap(createdBookDto -> Mono.empty());
    }

    @PatchMapping("/api/v1/books")
    public Mono<BookDto> updateBook(@Valid @RequestBody BookDto bookDto) {
        return bookService.update(
                        bookDto.getId(),
                        bookDto.getTitle(),
                        bookDto.getAuthor().getId(),
                        bookDto.getGenre().getId())
                .subscribeOn(workerPool);
    }

    @DeleteMapping("/api/v1/books/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBook(@PathVariable String id) {
        bookService.deleteById(id);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Mono<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException exception) {
        return Flux.just(exception.getBindingResult().getAllErrors())
                .collectMap(
                        error -> ((FieldError) error).getField(),
                        error -> ((FieldError) error).getDefaultMessage()
                )
                .subscribeOn(workerPool);
    }

    @ExceptionHandler(NotFoundException.class)
    public Mono<ResponseEntity<String>> handleNotFound(NotFoundException exception) {
        return Mono.just(ResponseEntity.notFound().build());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(WebExchangeBindException.class)
    public Mono<Map<String, String>> handleAll(WebExchangeBindException exception) {
        return Flux.fromIterable(exception.getBindingResult().getAllErrors())
                .subscribeOn(workerPool)
                .collectMap(
                        error -> ((FieldError) error).getField(),
                        error -> ((FieldError) error).getDefaultMessage()
                );
    }
}
