package ru.otus.hw.repository.reactive;

import org.bson.types.ObjectId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.annotation.DirtiesContext;
import reactor.test.StepVerifier;
import ru.otus.hw.model.Book;
import ru.otus.hw.model.Comment;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static ru.otus.hw.utils.TestUtils.*;


@DisplayName("Репозиторий на основе Spring Data для работы с комментариями ")
@DataMongoTest
class CommentRepositoryTest {
    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private MongoTemplate mt;

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    @DisplayName("должен загружать список всех комментариев по книге")
    @Test
    void shouldReturnCorrectCommentsList() {
        var actualComments = commentRepository.findAllCommentsByBookId(ID_1);

        StepVerifier
                .create(actualComments)
                .expectNextCount(2)
                .expectComplete()
                .verify();
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    @DisplayName("должен загружать комментарий по id")
    @Test
    void shouldReturnCorrectCommentById() {
        var actualComment = commentRepository.findById(ID_1);
        var expectedComment = mt.findById(ID_1, Comment.class);

        StepVerifier
                .create(actualComment)
                .assertNext(comment -> assertThat(comment).isEqualTo(expectedComment))
                .expectComplete()
                .verify();
    }

    @DisplayName("должен сохранять новый коментарий")
    @Test
    void shouldSaveNewComment() {
        var book = mt.findById(ID_1, Book.class);
        var newComment = new Comment(new ObjectId(), NEW_COMMENT_DESCRIPTION, book);
        var commentMono = commentRepository.save(newComment);

        StepVerifier
                .create(commentMono)
                .assertNext(comment -> assertNotNull(comment.getId()))
                .expectComplete()
                .verify();
    }

    @DisplayName("должен сохранять измененный комментарий")
    @Test
    void shouldSaveUpdatedComment() {
        var book = mt.findById(ID_1, Book.class);
        var expectedComment = new Comment(ID_1, UPDATING_COMMENT_DESCRIPTION, book);

        var commentMono = commentRepository.save(expectedComment);

        StepVerifier
                .create(commentMono)
                .assertNext(comment -> assertThat(comment.getDescription()).isEqualTo(UPDATING_COMMENT_DESCRIPTION))
                .expectComplete()
                .verify();
    }

    @DisplayName("должен удалять комментарий по id")
    @Test
    void shouldDeleteComment() {
        StepVerifier
                .create(commentRepository.findById(ID_1))
                .assertNext(book -> assertNotNull(book))
                .expectComplete()
                .verify();

        StepVerifier
                .create(commentRepository.deleteById(ID_1))
                .expectComplete()
                .verify();
    }
}