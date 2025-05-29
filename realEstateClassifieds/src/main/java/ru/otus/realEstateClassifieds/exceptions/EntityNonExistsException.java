package ru.otus.realEstateClassifieds.exceptions;

public class EntityNonExistsException extends RuntimeException {
    public EntityNonExistsException(String message) {
        super(message);
    }
}
