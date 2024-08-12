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
    public String findBookById(long id) {
        return bookService.findById(id)
                .map(bookDtoToStringConverter::bookDtoToString)
                .orElse("Book with id %d not found".formatted(id));
    }

    // bins newBook 1 1
    @ShellMethod(value = "Insert book", key = "bins")
    public String insertBook(String title, long authorId, long genreId) {
        var savedBook = bookService.insert(title, authorId, genreId);
        return bookDtoToStringConverter.bookDtoToString(savedBook);
    }

    // bupd 4 editedBook 3 2
    @ShellMethod(value = "Update book", key = "bupd")
    public String updateBook(long id, String title, long authorId, long genreId) {
        var savedBook = bookService.update(id, title, authorId, genreId);
        return bookDtoToStringConverter.bookDtoToString(savedBook);
    }

    // bdel 4
    @ShellMethod(value = "Delete book by id", key = "bdel")
    public void deleteBook(long id) {
        bookService.deleteById(id);
    }
}
