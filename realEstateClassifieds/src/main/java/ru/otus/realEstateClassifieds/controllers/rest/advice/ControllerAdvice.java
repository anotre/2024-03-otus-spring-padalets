package ru.otus.realEstateClassifieds.controllers.rest.advice;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.otus.realEstateClassifieds.exceptions.EntityAlreadyExistsException;
import ru.otus.realEstateClassifieds.exceptions.EntityNonExistsException;
import ru.otus.realEstateClassifieds.exceptions.EntityNotFoundException;

@RestControllerAdvice("ru.otus.realEstateClassifieds.controllers.rest")
public class ControllerAdvice {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handleNotFound(EntityNotFoundException exception) {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler({EntityAlreadyExistsException.class, EntityNonExistsException.class})
    public ResponseEntity<String> handleBadRequest(RuntimeException exception) {
        return ResponseEntity.badRequest().build();
    }
}
