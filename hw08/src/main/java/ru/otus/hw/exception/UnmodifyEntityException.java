package ru.otus.hw.exception;

public class UnmodifyEntityException extends RuntimeException {
    public UnmodifyEntityException(String message) {
        super(message);
    }
}
