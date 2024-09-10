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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
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
        var authors = authorService.findAll();
        var genres = genreService.findAll();
        var book = bookService.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Book with id %d not found".formatted(id)));

        if (!model.containsAttribute("book")) {
            model.addAttribute("book", book);
        } else {
            var bookWithError = (BookDtoRequest) model.getAttribute("book");
            bookWithError.setTitle(book.getTitle());
            model.addAttribute("book", bookWithError);
        }

        model.addAttribute("authors", authors);
        model.addAttribute("genres", genres);
        return "singleBook";
    }

    @PostMapping("/book/{id}")
    public String updateBook(@Valid @ModelAttribute("book") BookDtoRequest book,
                             BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectRequestWithError(book, bindingResult, redirectAttributes);
            return "redirect:/book/" + book.getId();
        }
        bookService.update(book.getId(), book.getTitle(), book.getAuthorId(), book.getGenreId());
        return "redirect:/book";
    }

    @GetMapping("/book/new")
    public String addBook(Model model) {
        if (!model.containsAttribute("book")) {
            model.addAttribute("book", new BookDtoRequest());
        }
        var authors = authorService.findAll();
        var genres = genreService.findAll();
        model.addAttribute("authors", authors);
        model.addAttribute("genres", genres);
        return "addBook";
    }

    @PostMapping("/book/new")
    public String addBook(@Valid @ModelAttribute("book") BookDtoRequest book,
                          BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectRequestWithError(book, bindingResult, redirectAttributes);
            return "redirect:/book/new";
        }
        bookService.insert(book.getTitle(), book.getAuthorId(), book.getGenreId());
        return "redirect:/book";
    }

    @GetMapping("/book/{id}/del")
    public String deleteBook(@PathVariable("id") long id, Model model) {
        var book = bookService.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Book with id %d not found".formatted(id)));
        model.addAttribute("book", book);
        return "deleteBook";
    }

    @PostMapping("/book/{id}/del")
    public String deleteBook(@PathVariable("id") long id) {
        bookService.deleteById(id);
        return "redirect:/book";
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ModelAndView handleNotFound(EntityNotFoundException ex) {
        ModelAndView modelAndView = new ModelAndView("error");
        modelAndView.addObject("message", ex.getMessage());
        return modelAndView;
    }

    private void redirectRequestWithError(BookDtoRequest bookDtoRequest,
                                          BindingResult bindingResult,
                                          RedirectAttributes redirectAttributes) {
        redirectAttributes.getFlashAttributes().clear();
        redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.book", bindingResult);
        redirectAttributes.addFlashAttribute("book", bookDtoRequest);
    }
}