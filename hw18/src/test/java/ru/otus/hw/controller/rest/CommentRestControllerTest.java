package ru.otus.hw.controller.rest;

import org.bson.types.ObjectId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.converter.dto.CommentDtoConverter;
import ru.otus.hw.dto.CommentDto;
import ru.otus.hw.dto.CommentDtoRequest;
import ru.otus.hw.model.Comment;
import ru.otus.hw.repository.reactive.CommentRepository;

import java.time.Duration;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static ru.otus.hw.utils.TestUtils.*;

@DisplayName("REST-контроллер комментариев")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CommentRestControllerTest {
    @Autowired
    private CommentDtoConverter converter;

    @Autowired
    private WebTestClient webTestClient;

    @LocalServerPort
    private int port;

    @MockBean
    CommentRepository commentRepository;

    @DisplayName("должен возвращать корректный список комментариев по айди книги")
    @Test
    void shouldReturnCorrectCommentsListByBookId() {
        given(commentRepository.findAllCommentsByBookId(ID_1)).willReturn(Flux.just(COMMENT_1, COMMENT_4));

        var client = WebClient.create(String.format("http://localhost:%d", port));
        var result = client
                .get().uri("/api/v1/comment?bookId=" + ID_1)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToFlux(CommentDto.class)
                .timeout(Duration.ofSeconds(3))
                .collectList()
                .block();

        verify(commentRepository, times(1)).findAllCommentsByBookId(ID_1);
        assertThat(result).containsExactlyElementsOf(List.of(COMMENT_DTO_1, COMMENT_DTO_4));
    }

    @DisplayName("должен возвращать комментарий по айди")
    @Test
    void shouldReturnCommentById() {
        given(commentRepository.findById(ID_1)).willReturn(Mono.just(COMMENT_1));

        var client = WebClient.create(String.format("http://localhost:%d", port));
        var result = client
                .get().uri("/api/v1/comment/" + ID_1)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(CommentDto.class)
                .timeout(Duration.ofSeconds(3))
                .block();

        verify(commentRepository, times(1)).findById(ID_1);
        assertThat(result).isEqualTo(COMMENT_DTO_1);
    }

    @DisplayName("должен обновлять комментарий")
    @Test
    void shouldUpdateComment() {
        given(commentRepository.findById(ID_1)).willReturn(Mono.just(COMMENT_1));
        given(commentRepository.save(any())).willReturn(Mono.just(UPDATING_COMMENT));
        var comment = new CommentDtoRequest(ID_1, UPDATING_COMMENT_DESCRIPTION, ID_1);

        var client = WebClient.create(String.format("http://localhost:%d", port));
        var result = client
                .put().uri("/api/v1/comment/" + ID_1)
                .body(BodyInserters.fromValue(comment))
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(CommentDto.class)
                .timeout(Duration.ofSeconds(6))
                .block();

        verify(commentRepository, times(1)).save(any());
        assertThat(result.getDescription()).isEqualTo(UPDATING_COMMENT_DESCRIPTION);
    }

    @DisplayName("должен сохранять новый комментарий")
    @Test
    void shouldSaveNewComment() {
        var comment = new Comment(new ObjectId(), NEW_COMMENT_DESCRIPTION, BOOK_1);
        var commentDtoRequest = new CommentDtoRequest(comment.getId(), NEW_COMMENT_DESCRIPTION, ID_1);
        given(commentRepository.save(any())).willReturn(Mono.just(comment));

        var client = WebClient.create(String.format("http://localhost:%d", port));
        var result = client
                .post().uri("/api/v1/comment")
                .accept(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(commentDtoRequest))
                .retrieve()
                .bodyToMono(CommentDto.class)
                .timeout(Duration.ofSeconds(3))
                .block();

        verify(commentRepository, times(1)).save(any());
        assertThat(result.getId()).isEqualTo(comment.getId());
    }

    @DisplayName("должен удалять комментарий")
    @Test
    void shouldDeleteComment() {
        given(commentRepository.deleteById(ID_1)).willReturn(Mono.empty());

        var client = WebClient.create(String.format("http://localhost:%d", port));
        var result = client
                .delete().uri("/api/v1/comment/" + ID_1)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(Void.class)
                .timeout(Duration.ofSeconds(3))
                .block();

        verify(commentRepository, times(1)).deleteById(ID_1);
        assertThat(result).isNull();
    }
}