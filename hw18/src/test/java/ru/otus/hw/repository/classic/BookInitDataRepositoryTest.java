package ru.otus.hw.repository.classic;

import org.bson.types.ObjectId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.annotation.DirtiesContext;
import ru.otus.hw.model.Author;
import ru.otus.hw.model.Book;
import ru.otus.hw.model.Genre;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.otus.hw.utils.TestUtils.*;

@DisplayName("Репозиторий на основе Spring Data для работы с книгами")
@DataMongoTest
class BookInitDataRepositoryTest {
    @Autowired
    private BookInitDataRepository bookRepository;

    @Autowired
    private MongoTemplate mt;

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    @DisplayName("должен загружать список всех книг")
    @Test
    void shouldReturnCorrectBooksList() {
        var actualBooks = bookRepository.findAll();

        var expectedBook1 = mt.findById(ID_1, Book.class);
        var expectedBook2 = mt.findById(ID_2, Book.class);
        var expectedBook3 = mt.findById(ID_3, Book.class);

        assertThat(actualBooks).containsExactlyElementsOf(List.of(expectedBook1, expectedBook2, expectedBook3));
        actualBooks.forEach(System.out::println);
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    @DisplayName("должен загружать книгу по id")
    @Test
    void shouldReturnCorrectBookById() {
        var actualBook = bookRepository.findById(ID_1);
        var expectedBook = mt.findById(ID_1, Book.class);
        assertThat(actualBook).isPresent()
                .get()
                .isEqualTo(expectedBook);
    }

    @DisplayName("должен сохранять новую книгу")
    @Test
    void shouldSaveNewBook() {
        var author = mt.findById(ID_1, Author.class);
        var genre = mt.findById(ID_1, Genre.class);
        var newBook = new Book(new ObjectId(), NEW_BOOK_TITLE, author, genre);

        bookRepository.save(newBook);

        var books = mt.findAll(Book.class);
        int size = books.size();
        assertThat(size).isEqualTo(4);

        var savedBook = books.get(size - 1);

        assertThat(savedBook.getTitle()).isEqualTo(NEW_BOOK_TITLE);
    }

    @DisplayName("должен сохранять измененную книгу")
    @Test
    void shouldSaveUpdatedBook() {
        var author = mt.findById(ID_1, Author.class);
        var genre = mt.findById(ID_1, Genre.class);
        var expectedBook = new Book(ID_1, UPDATING_BOOK_TITLE, author, genre);

        assertThat(bookRepository.findById(expectedBook.getId()))
                .isPresent()
                .get()
                .isNotEqualTo(expectedBook);

        var returnedBook = bookRepository.save(expectedBook);
        assertThat(returnedBook).isNotNull()
                .usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(expectedBook);

        assertThat(bookRepository.findById(returnedBook.getId()))
                .isPresent()
                .get()
                .isEqualTo(returnedBook);
    }

    @DisplayName("должен удалять книгу по id")
    @Test
    void shouldDeleteBook() {
        assertThat(bookRepository.findById(ID_1)).isPresent();
        bookRepository.deleteById(ID_3);
        assertThat(bookRepository.findById(ID_3)).isEmpty();
    }
}