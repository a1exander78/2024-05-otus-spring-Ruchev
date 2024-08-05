package ru.otus.hw.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.converter.AuthorToDtoConverterImpl;
import ru.otus.hw.converter.BookToDtoConverterImpl;
import ru.otus.hw.converter.GenreToDtoConverterImpl;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.GenreDto;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional(propagation = Propagation.NEVER)
@Import({BookServiceImpl.class, AuthorServiceImpl.class, GenreServiceImpl.class,
        BookToDtoConverterImpl.class, AuthorToDtoConverterImpl.class, GenreToDtoConverterImpl.class})
@DisplayName("Сервис для работы с книгами")
@DataMongoTest
public class BookServiceTest {
    private static final String ID_1 = "1";
    private static final String ID_2 = "2";
    private static final String ID_3 = "3";

    private static final AuthorDto AUTHOR_1 = new AuthorDto(ID_1, "Author_Test_1");
    private static final AuthorDto AUTHOR_2 = new AuthorDto(ID_2, "Author_Test_2");
    private static final AuthorDto AUTHOR_3 = new AuthorDto(ID_3, "Author_Test_3");

    private static final GenreDto GENRE_1 = new GenreDto(ID_1, "Genre_Test_1");
    private static final GenreDto GENRE_2 = new GenreDto(ID_2, "Genre_Test_2");
    private static final GenreDto GENRE_3 = new GenreDto(ID_3, "Genre_Test_3");

    private static final BookDto BOOK_1 = new BookDto(ID_1, "Book_Test_1", AUTHOR_1, GENRE_1);
    private static final BookDto BOOK_2 = new BookDto(ID_2, "Book_Test_2", AUTHOR_2, GENRE_2);
    private static final BookDto BOOK_3 = new BookDto(ID_3, "Book_Test_3", AUTHOR_3, GENRE_3);

    private static final String NEW_TITLE = "New_Book";

    private static final String UPDATING_TITLE = "Updating_Title";

    @Autowired
    private BookService bookService;

    @DisplayName("должен загружать список всех книг")
    @Test
    void shouldReturnCorrectBooksList() {
        assertThat(bookService.findAll()).containsExactlyElementsOf(List.of(BOOK_1, BOOK_2, BOOK_3));
    }

    @DisplayName("должен загружать книгу по id")
    @Test
    void shouldReturnCorrectBookById() {
        assertThat(bookService.findById(ID_1)).contains(BOOK_1);
    }

    @DisplayName("должен сохранять новую книгу")
    @Test
    void shouldSaveNewBook() {
        assertThat(bookService.findAll().size()).isEqualTo(3);
        bookService.insert(NEW_TITLE, ID_1, ID_1);
        var newBook = bookService.findAll().stream().skip(3).findFirst();
        assertThat(newBook.isPresent());
        assertThat(newBook.get().getTitle().equals(NEW_TITLE));
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @DisplayName("должен сохранять измененную книгу")
    @Test
    void shouldSaveUpdatedBook() {
        assertThat(bookService.findById(ID_1)).contains(BOOK_1);
        bookService.update(ID_1, UPDATING_TITLE, ID_1, ID_1);
        assertThat(bookService.findById(ID_1).orElseGet(() -> BOOK_1)).isNotEqualTo(BOOK_1);
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @DisplayName("должен удалять книгу по id")
    @Test
    void shouldDeleteBook() {
        assertThat(bookService.findById(ID_1)).contains(BOOK_1);
        bookService.deleteById(ID_1);
        assertThat(bookService.findById(ID_1)).isEmpty();
    }
}
