package ru.otus.project.exception;

public class CustomQueryException extends RuntimeException {
    public CustomQueryException(String message) {
        super(message);
    }
}
