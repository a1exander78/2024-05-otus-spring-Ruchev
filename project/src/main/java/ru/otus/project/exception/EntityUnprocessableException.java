package ru.otus.project.exception;

public class EntityUnprocessableException extends RuntimeException {
    public EntityUnprocessableException(String message) {
        super(message);
    }
}
