package ru.otus.hw.controller.rest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.hw.dto.BookDtoRequest;
import ru.otus.hw.exception.EntityNotFoundException;
import ru.otus.hw.service.AuthorService;
import ru.otus.hw.service.BookService;
import ru.otus.hw.service.GenreService;

@RequiredArgsConstructor
@RestController
public class BookRestController {
    private final BookService bookService;

    private final AuthorService authorService;

    private final GenreService genreService;

    @PostMapping("/api/v1/book/new")
    public void addBook(@Valid @RequestBody BookDtoRequest book,
                           BindingResult bindingResult, Model model) {
        if (!bindingResult.hasErrors()) {
            bookService.insert(book.getTitle(), book.getAuthorId(), book.getGenreId());
        }
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handleNotFound(EntityNotFoundException ex) {
        return ResponseEntity.badRequest().body("error");
    }

    private void fillModelWithCatalogData(Model model) {
        model.addAttribute("authors", authorService.findAll());
        model.addAttribute("genres", genreService.findAll());
    }
}