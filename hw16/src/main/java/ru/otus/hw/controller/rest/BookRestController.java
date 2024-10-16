package ru.otus.hw.controller.rest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.BookDtoRequest;
import ru.otus.hw.exception.EntityNotFoundException;
import ru.otus.hw.service.AuthorService;
import ru.otus.hw.service.BookService;
import ru.otus.hw.service.GenreService;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class BookRestController {
    private final BookService bookService;

    private final AuthorService authorService;

    private final GenreService genreService;

    @GetMapping("/api/v1/book/")
    public List<BookDto> readAllBooks() {
        return bookService.findAll();
    }

    @GetMapping("/api/v1/book/{id}")
    public BookDto readBook(@PathVariable("id") long id) {
        var book = bookService.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Book with id %d not found".formatted(id)));
        return book;
    }

    @PutMapping("/api/v1/book/{id}")
    public void updateBook(@Valid @RequestBody BookDtoRequest book,
                             BindingResult bindingResult) {
        if (!bindingResult.hasErrors()) {
            bookService.update(book.getId(), book.getTitle(), book.getAuthorId(), book.getGenreId());
        }
    }

    @PostMapping("/api/v1/book")
    public void addBook(@Valid @RequestBody BookDtoRequest book,
                           BindingResult bindingResult) {
        if (!bindingResult.hasErrors()) {
            bookService.insert(book.getTitle(), book.getAuthorId(), book.getGenreId());
        }
    }

    @DeleteMapping("/api/v1/book/{id}")
    public void deleteBook(@PathVariable("id") long id) {
        bookService.deleteById(id);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handleNotFound(EntityNotFoundException ex) {
        return ResponseEntity.badRequest().body("error");
    }
}