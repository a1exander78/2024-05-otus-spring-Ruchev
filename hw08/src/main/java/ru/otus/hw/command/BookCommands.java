package ru.otus.hw.command;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.hw.converter.toString.BookDtoToStringConverter;
import ru.otus.hw.service.BookService;

import java.util.stream.Collectors;

@SuppressWarnings({"SpellCheckingInspection", "unused"})
@RequiredArgsConstructor
@ShellComponent
public class BookCommands {
    private final BookService bookService;

    private final BookDtoToStringConverter bookDtoToStringConverter;

    @ShellMethod(value = "Find all books", key = "ab")
    public String findAllBooks() {
        return bookService.findAll().stream()
                .map(bookDtoToStringConverter::bookDtoToString)
                .collect(Collectors.joining("," + System.lineSeparator()));
    }

    @ShellMethod(value = "Find book by id", key = "bbid")
    public String findBookById(String id) {
        return bookService.findById(id)
                .map(bookDtoToStringConverter::bookDtoToString)
                .orElse("Book with id %s not found".formatted(id));
    }

    // bins newBook 1 1
    @ShellMethod(value = "Insert book", key = "bins")
    public String insertBook(String title, String authorId, String genreId) {
        var savedBook = bookService.insert(title, authorId, genreId);
        return bookDtoToStringConverter.bookDtoToString(savedBook);
    }

    // bupd 4 editedBook 3 2
    @ShellMethod(value = "Update book", key = "bupd")
    public String updateBook(String id, String title, String authorId, String genreId) {
        var savedBook = bookService.update(id, title, authorId, genreId);
        return bookDtoToStringConverter.bookDtoToString(savedBook);
    }

    // bupd -f 4 editedBook 3 2
    @ShellMethod(value = "Force update book (update inner comments)", key = "bupd -f")
    public String forceUpdateBook(String id, String title, String authorId, String genreId) {
        var savedBook = bookService.forceUpdate(id, title, authorId, genreId);
        return bookDtoToStringConverter.bookDtoToString(savedBook);
    }

    // bdel 4
    @ShellMethod(value = "Delete book by id", key = "bdel")
    public void deleteBook(String id) {
        bookService.deleteById(id);
    }
}
