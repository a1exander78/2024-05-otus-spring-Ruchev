package ru.otus.hw.repository.reactive;

import org.bson.types.ObjectId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.annotation.DirtiesContext;
import reactor.test.StepVerifier;
import ru.otus.hw.model.Author;
import ru.otus.hw.model.Book;
import ru.otus.hw.model.Genre;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static ru.otus.hw.utils.TestUtils.*;

@DisplayName("Репозиторий на основе Reactive Mongo для работы с книгами")
@DataMongoTest
class BookRepositoryTest {
    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private MongoTemplate mt;

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    @DisplayName("должен загружать список всех книг")
    @Test
    void shouldReturnCorrectBooksList() {
        var actualBooks = bookRepository.findAll();

        StepVerifier
                .create(actualBooks)
                .expectNextCount(3)
                .expectComplete()
                .verify();
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    @DisplayName("должен загружать книгу по id")
    @Test
    void shouldReturnCorrectBookById() {
        var actualBook = bookRepository.findById(ID_1);
        var expectedBook = mt.findById(ID_1, Book.class);

        StepVerifier
                .create(actualBook)
                .assertNext(book -> assertThat(book).isEqualTo(expectedBook))
                .expectComplete()
                .verify();
    }

    @DisplayName("должен сохранять новую книгу")
    @Test
    void shouldSaveNewBook() {
        var author = mt.findById(ID_1, Author.class);
        var genre = mt.findById(ID_1, Genre.class);
        var newBook = new Book(new ObjectId(), NEW_BOOK_TITLE, author, genre);

        var bookMono = bookRepository.save(newBook);

        StepVerifier
                .create(bookMono)
                .assertNext(book -> assertNotNull(book.getId()))
                .expectComplete()
                .verify();
    }

    @DisplayName("должен сохранять измененную книгу")
    @Test
    void shouldSaveUpdatedBook() {
        var author = mt.findById(ID_1, Author.class);
        var genre = mt.findById(ID_1, Genre.class);
        var expectedBook = new Book(ID_1, UPDATING_BOOK_TITLE, author, genre);

        var bookMono = bookRepository.save(expectedBook);

        StepVerifier
                .create(bookMono)
                .assertNext(book -> assertThat(book.getTitle()).isEqualTo(UPDATING_BOOK_TITLE))
                .expectComplete()
                .verify();
    }

    @DisplayName("должен удалять книгу по id")
    @Test
    void shouldDeleteBook() {
        StepVerifier
                .create(bookRepository.findById(ID_1))
                .assertNext(book -> assertNotNull(book))
                .expectComplete()
                .verify();

        StepVerifier
                .create(bookRepository.deleteById(ID_1))
                .expectComplete()
                .verify();
    }
}