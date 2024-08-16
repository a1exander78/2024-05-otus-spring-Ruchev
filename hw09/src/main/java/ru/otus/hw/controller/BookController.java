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
import ru.otus.hw.exception.EntityNotFoundException;
import ru.otus.hw.service.AuthorService;
import ru.otus.hw.service.BookService;
import ru.otus.hw.service.GenreService;

@RequiredArgsConstructor
@Controller
public class BookController {
    private final BookService bookService;

    private final AuthorService authorService;

    private final GenreService genreService;

    @GetMapping("/")
    public String startPage() {
        return "start";
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
        var authors = authorService.findAll();
        var genres = genreService.findAll();
        model.addAttribute("book", book);
        model.addAttribute("authors", authors);
        model.addAttribute("genres", genres);
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

    @GetMapping("/book/new")
    public String addBook(Model model) {
        var authors = authorService.findAll();
        var genres = genreService.findAll();
        model.addAttribute("authors", authors);
        model.addAttribute("genres", genres);
        return "addBook";
    }

    @PostMapping("/book/new")
    public String addBook(@RequestParam("title") String title,
                          @RequestParam("authorId") long authorId,
                          @RequestParam("genreId") long genreId) {
        bookService.insert(title, authorId, genreId);
        return "redirect:/book";
    }

    @GetMapping("/book/{id}/del")
    public String deleteBook(@PathVariable("id") long id) {
        return "deleteBook";
    }

    @PostMapping("/book/{id}/del")
    public String deleteBook(@PathVariable("id") long id, Model model) {
        bookService.deleteById(id);
        return "redirect:/book";
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handleNotFound(EntityNotFoundException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }
}
