package ru.otus.hw.services;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.models.Book;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.GenreRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.BDDMockito.given;

@Transactional(propagation = Propagation.NEVER)
@DisplayName("Сервис для работы с книгами")
@DataJpaTest
@Import(BookServiceImpl.class)
public class BookServiceTest {

    @MockBean
    private AuthorRepository authorRepository;

    @MockBean
    private GenreRepository genreRepository;

    @MockBean
    private BookRepository bookRepository;

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
        var expectedBook = new Book(1L, "ExpectedBook", null, null);
        long expectedBookId = expectedBook.getId();
        given(bookRepository.findById(expectedBookId)).willReturn(Optional.of(expectedBook));
        assertThat(bookService.findById(expectedBookId)).contains(expectedBook);
    }
}
