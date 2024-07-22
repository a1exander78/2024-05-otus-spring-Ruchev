package ru.otus.hw.services;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;
import ru.otus.hw.repositories.JpaAuthorRepository;
import ru.otus.hw.repositories.JpaBookRepository;
import ru.otus.hw.repositories.JpaGenreRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

@Transactional(propagation = Propagation.NEVER)
@DisplayName("Сервис для работы с книгами")
@DataJpaTest
@Import({BookServiceImpl.class, JpaAuthorRepository.class, JpaGenreRepository.class, JpaBookRepository.class})
public class BookServiceTest {
    private static final long ID_1 = 1L;
    private static final long ID_2 = 2L;
    private static final long ID_3 = 3L;
    private static final long ID_4 = 4L;

    private static final Author AUTHOR_1 = new Author(ID_1, "Author_Test_1");
    private static final Author AUTHOR_3 = new Author(ID_3, "Author_Test_3");

    private static final Genre GENRE_1 = new Genre(ID_1, "Genre_Test_1");
    private static final Genre GENRE_2 = new Genre(ID_2, "Genre_Test_2");

    private static final Book BOOK_1 = new Book(ID_1, "Book_Test_1", AUTHOR_1, GENRE_1);

    private static final Book NEW_BOOK = new Book(ID_4, "New_Book", AUTHOR_3, GENRE_2);

    private static final String UPDATING_TITLE = "Updating_Title";

    @Autowired
    private BookService bookService;

    @DisplayName("должен загружать список всех книг")
    @Test
    void shouldReturnCorrectBooksList() {
        assertThatCode(() -> bookService.findAll()).doesNotThrowAnyException();
    }

    @DisplayName("должен загружать книгу по id")
    @Test
    void shouldReturnCorrectBookById() {
        assertThat(bookService.findById(ID_1)).contains(BOOK_1);
    }

    @DisplayName("должен сохранять новую книгу")
    @Test
    void shouldSaveNewBook() {
       assertThat(bookService.findById(NEW_BOOK.getId())).isEmpty();
       bookService.insert(NEW_BOOK.getTitle(), NEW_BOOK.getAuthor().getId(), NEW_BOOK.getGenre().getId());
       assertThat(bookService.findById(NEW_BOOK.getId())).contains(NEW_BOOK);
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
    @DisplayName("должен удалять книгу по id ")
    @Test
    void shouldDeleteBook() {
        assertThat(bookService.findById(ID_1)).contains(BOOK_1);
        bookService.deleteById(ID_1);
        assertThat(bookService.findById(ID_1)).isEmpty();
    }
}
