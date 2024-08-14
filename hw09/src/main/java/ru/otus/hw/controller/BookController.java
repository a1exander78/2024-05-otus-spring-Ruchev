package ru.otus.hw.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.otus.hw.converter.dto.BookDtoConverter;
import ru.otus.hw.exception.EntityNotFoundException;
import ru.otus.hw.service.BookService;

@RequiredArgsConstructor
@Controller
public class BookController {
    private final BookService bookService;

    private final BookDtoConverter converter;

    @GetMapping("/")
    public String startPage() {
        return "pageStart";
    }

    @GetMapping("/book")
    public String readAllBooks(Model model) {
        var books = bookService.findAll();
        model.addAttribute("books", books);
        return "allBooks";
    }

    @GetMapping("/book/{id}")
    public String readBook(@PathVariable("id") long id, Model model) {
        var book = bookService.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Book with id %d not found".formatted(id)));
        model.addAttribute("book", book);
        return "singleBook";
    }

    @PostMapping("/book")
    public String updateBook(@RequestParam("id") long id,
                             @RequestParam("title") String title,
                             @RequestParam("authorId") long authorId,
                             @RequestParam("genreId") long genreId) {
        bookService.update(id, title, authorId, genreId);
        return "redirect:/book";
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handleNotFound(EntityNotFoundException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }
}
