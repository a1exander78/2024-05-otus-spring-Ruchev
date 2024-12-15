package ru.otus.project.exception.handler;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.otus.project.exception.EntityNotFoundException;
import ru.otus.project.exception.EntityUnprocessableException;
import ru.otus.project.exception.DataNotValidException;
import ru.otus.project.exception.PasswordException;
import ru.otus.project.exception.CartException;

@ControllerAdvice
public class ControllerExceptionHandler {
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handleNotFound(EntityNotFoundException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @ExceptionHandler(EntityUnprocessableException.class)
    public ResponseEntity<String> handleUnprocessableEntity(EntityUnprocessableException ex) {
        return ResponseEntity.unprocessableEntity().body(ex.getMessage());
    }

    @ExceptionHandler(DataNotValidException.class)
    public ResponseEntity<String> handleInValidException(DataNotValidException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @ExceptionHandler(PasswordException.class)
    public ResponseEntity<String> handlePasswordException(PasswordException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @ExceptionHandler(CartException.class)
    public ResponseEntity<String> handleCartException(CartException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }
}
