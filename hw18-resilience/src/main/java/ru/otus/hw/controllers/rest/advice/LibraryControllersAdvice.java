package ru.otus.hw.controllers.rest.advice;

import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.otus.hw.controllers.exceptions.NotFoundException;

@RestControllerAdvice("ru.otus.hw.controllers.rest")
public class LibraryControllersAdvice {
    @ExceptionHandler({CallNotPermittedException.class})
    public ResponseEntity<String> handleOpenedCircuitBreaker(RuntimeException exception) {
        return ResponseEntity.internalServerError().build();
    }

    @ExceptionHandler({NotFoundException.class})
    public ResponseEntity<String> handleNotFound(RuntimeException exception) {
        return ResponseEntity.notFound().build();
    }
}
