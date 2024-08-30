package ru.otus.hw.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import ru.otus.hw.dto.BookDtoRequest;
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

    @GetMapping({"/", "/api/v1/main"})
    public String startPage() {
        return "start";
    }

    @GetMapping("/api/v1/book")
    public String readAllBooks(Model model) {
        var books = bookService.findAll();
        model.addAttribute("books", books);
        return "allBooks";
    }

    @GetMapping("/api/v1/book/{id}")
    public String readBook(@PathVariable("id") long id, Model model) {
        var book = bookService.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Book with id %d not found".formatted(id)));
        model.addAttribute("book", book);
        fillModelWithCatalogData(model);
        return "singleBook";
    }

    @PostMapping("/api/v1/book/{id}")
    public String updateBook(@Valid @ModelAttribute("book") BookDtoRequest book,
                             BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            fillModelWithCatalogData(model);
            return "singleBook";
        }
        bookService.update(book.getId(), book.getTitle(), book.getAuthorId(), book.getGenreId());
        return "redirect:/api/v1/book";
    }

    @GetMapping("/api/v1/book/new")
    public String addBook(Model model) {
        fillModelWithCatalogData(model);
        model.addAttribute("book", new BookDtoRequest());
        return "addBook";
    }

//    @PostMapping("/api/v1/book/new")
//    public String addBook(@Valid @ModelAttribute("book") BookDtoRequest book,
//                          BindingResult bindingResult, Model model) {
//        if (bindingResult.hasErrors()) {
//            fillModelWithCatalogData(model);
//            return "addBook";
//        }
//        bookService.insert(book.getTitle(), book.getAuthorId(), book.getGenreId());
//        return "redirect:/api/v1/book";
//    }

    @GetMapping("/api/v1/book/{id}/del")
    public String deleteBook(@PathVariable("id") long id, Model model) {
        var book = bookService.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Book with id %d not found".formatted(id)));
        model.addAttribute("book", book);
        return "deleteBook";
    }

    @PostMapping("/api/v1/book/{id}/del")
    public String deleteBook(@PathVariable("id") long id) {
        bookService.deleteById(id);
        return "redirect:/api/v1/book";
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ModelAndView handleNotFound(EntityNotFoundException ex) {
        ModelAndView modelAndView = new ModelAndView("error");
        modelAndView.addObject("message", ex.getMessage());
        return modelAndView;
    }

    private void fillModelWithCatalogData(Model model) {
        model.addAttribute("authors", authorService.findAll());
        model.addAttribute("genres", genreService.findAll());
    }
}