package ru.otus.hw.command;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.hw.service.BookService;

import java.util.stream.Collectors;

@SuppressWarnings({"SpellCheckingInspection", "unused"})
@RequiredArgsConstructor
@ShellComponent
public class BookCommands {

    private final BookService bookService;

    @ShellMethod(value = "Find all books", key = "ab")
    public String findAllBooks() {
        return bookService.findAll().stream()
                .map(bookService::bookDtoToString)
                .collect(Collectors.joining("," + System.lineSeparator()));
    }

    @ShellMethod(value = "Find book by id", key = "bbid")
    public String findBookById(String id) {
        return bookService.findById(id)
                .map(bookService::bookDtoToString)
                .orElse("Book with id %s not found".formatted(id));
    }

    // bins newBook 1 1
    @ShellMethod(value = "Insert book", key = "bins")
    public String insertBook(String title, String authorId, String genreId) {
        var savedBook = bookService.insert(title, authorId, genreId);
        return bookService.bookDtoToString(savedBook);
    }

    // bupd 4 editedBook 3 2
    @ShellMethod(value = "Update book", key = "bupd")
    public String updateBook(String id, String title, String authorId, String genreId) {
        var savedBook = bookService.update(id, title, authorId, genreId);
        return bookService.bookDtoToString(savedBook);
    }

    // bdel 4
    @ShellMethod(value = "Delete book by id", key = "bdel")
    public void deleteBook(String id) {
        bookService.deleteById(id);
    }
}