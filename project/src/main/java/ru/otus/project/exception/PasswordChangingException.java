package ru.otus.project.exception;

public class PasswordChangingException extends RuntimeException {
    public PasswordChangingException(String message) {
        super(message);
    }
}
