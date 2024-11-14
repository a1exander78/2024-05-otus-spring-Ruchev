package ru.otus.hw.controller.rest;

import org.bson.types.ObjectId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.converter.dto.BookDtoConverter;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.BookDtoRequest;
import ru.otus.hw.model.Book;
import ru.otus.hw.repository.reactive.BookRepository;

import java.time.Duration;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static ru.otus.hw.utils.TestUtils.*;


@DisplayName("REST-контроллер книг")
@AutoConfigureDataMongo
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BookRestControllerTest {
    @Autowired
    private BookDtoConverter converter;

    @Autowired
    private WebTestClient webTestClient;

    @LocalServerPort
    private int port;

    @MockBean
    private BookRepository bookRepository;

    @DisplayName("должен возвращать корректный список книг")
    @AutoConfigureWebTestClient
    @Test
    void shouldReturnCorrectBooksList() {
        given(bookRepository.findAll()).willReturn(Flux.just(BOOK_1, BOOK_2, BOOK_3));

        var client = WebClient.create(String.format("http://localhost:%d", port));
        var result = client
                .get().uri("/api/v1/book/")
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToFlux(BookDto.class)
                .timeout(Duration.ofSeconds(3))
                .collectList()
                .block();

        verify(bookRepository, times(1)).findAll();
        assertThat(result).containsExactlyElementsOf(List.of(BOOK_DTO_1, BOOK_DTO_2, BOOK_DTO_3));
    }

    @DisplayName("должен возвращать книгу по айди")
    @Test
    void shouldReturnBookById() {
        given(bookRepository.findById(ID_1)).willReturn(Mono.just(BOOK_1));

        var client = WebClient.create(String.format("http://localhost:%d", port));
        var result = client
                .get().uri("/api/v1/book/" + ID_1)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(BookDto.class)
                .timeout(Duration.ofSeconds(3))
                .block();

        verify(bookRepository, times(1)).findById(ID_1);
        assertThat(result).isEqualTo(BOOK_DTO_1);
    }

    @DisplayName("должен обновлять книгу")
    @Test
    void shouldUpdateBook() {
        given(bookRepository.save(any())).willReturn(Mono.just(UPDATING_BOOK));
        var book = new BookDtoRequest(ID_1, UPDATING_BOOK_TITLE, ID_1, ID_1);

        var client = WebClient.create(String.format("http://localhost:%d", port));
        var result = client
                .put().uri("/api/v1/book/" + ID_1)
                .body(BodyInserters.fromValue(book))
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(BookDto.class)
                .timeout(Duration.ofSeconds(6))
                .block();

        verify(bookRepository, times(1)).save(any());
        assertThat(result.getTitle()).isEqualTo(UPDATING_BOOK_TITLE);
    }

    @DisplayName("должен сохранять новую книгу")
    @Test
    void shouldSaveNewBook() {
        var book = new Book(new ObjectId(), NEW_BOOK_TITLE, AUTHOR_1, GENRE_1);
        var bookDtoRequest = new BookDtoRequest(book.getId(), NEW_BOOK_TITLE, ID_1, ID_1);
        given(bookRepository.save(any())).willReturn(Mono.just(book));

        var client = WebClient.create(String.format("http://localhost:%d", port));
        var result = client
                .post().uri("/api/v1/book")
                .accept(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(bookDtoRequest))
                .retrieve()
                .bodyToMono(BookDto.class)
                .timeout(Duration.ofSeconds(3))
                .block();

        verify(bookRepository, times(1)).save(any());
        assertThat(result.getId()).isEqualTo(book.getId());
    }

    @DisplayName("должен удалять книгу")
    @Test
    void shouldDeleteBook() {
        given(bookRepository.deleteById(ID_1)).willReturn(Mono.empty());

        var client = WebClient.create(String.format("http://localhost:%d", port));
        var result = client
                .delete().uri("/api/v1/book/" + ID_1)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(Void.class)
                .timeout(Duration.ofSeconds(3))
                .block();

        verify(bookRepository, times(1)).deleteById(ID_1);
        assertThat(result).isNull();
    }
}