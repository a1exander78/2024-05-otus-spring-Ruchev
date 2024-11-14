package ru.otus.hw.repository.classic;

import org.bson.types.ObjectId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.annotation.DirtiesContext;
import ru.otus.hw.model.Book;
import ru.otus.hw.model.Comment;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.otus.hw.utils.TestUtils.*;

@DisplayName("Репозиторий на основе Spring Data для работы с комментариями ")
@DataMongoTest
class CommentInitDataRepositoryTest {
    @Autowired
    private CommentInitDataRepository commentRepository;

    @Autowired
    private MongoTemplate mt;

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    @DisplayName("должен загружать список всех комментариев по книге")
    @Test
    void shouldReturnCorrectCommentsList() {
        var actualComments = commentRepository.findAllCommentsByBookId(ID_1);

        var expectedComment1 = mt.findById(ID_1, Comment.class);
        var expectedComment2 = mt.findById(ID_4, Comment.class);

        assertThat(actualComments).containsExactlyElementsOf(List.of(expectedComment1, expectedComment2));
        actualComments.forEach(System.out::println);
    }

    @DisplayName("должен загружать комментарий по id")
    @Test
    void shouldReturnCorrectCommentById() {
        var actualComment = commentRepository.findById(ID_1);
        var expectedComment = mt.findById(ID_1, Comment.class);

        assertThat(actualComment).isPresent()
                .get()
                .isEqualTo(expectedComment);
    }

    @DisplayName("должен сохранять новый коментарий")
    @Test
    void shouldSaveNewComment() {
        var book = mt.findById(ID_1, Book.class);

        var newComment = new Comment(new ObjectId(), NEW_COMMENT_DESCRIPTION, book);

        commentRepository.save(newComment);

        var comments = commentRepository.findAllCommentsByBookId(ID_1);
        int size = comments.size();
        assertThat(size).isEqualTo(3);

        var savedComment = comments.get(size - 1);

        assertThat(savedComment.getDescription()).isEqualTo(NEW_COMMENT_DESCRIPTION);
    }

    @DisplayName("должен сохранять измененный комментарий")
    @Test
    void shouldSaveUpdatedComment() {
        var book = mt.findById(ID_1, Book.class);
        var expectedComment = new Comment(ID_1, UPDATING_COMMENT_DESCRIPTION, book);

        assertThat(commentRepository.findById(expectedComment.getId()))
                .isPresent()
                .get()
                .isNotEqualTo(expectedComment);

        var returnedComment = commentRepository.save(expectedComment);
        assertThat(returnedComment).isNotNull()
                .usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(expectedComment);

        assertThat(commentRepository.findById(returnedComment.getId()))
                .isPresent()
                .get()
                .isEqualTo(returnedComment);
    }

    @DisplayName("должен удалять комментарий по id")
    @Test
    void shouldDeleteComment() {
        assertThat(commentRepository.findById(ID_3)).isPresent();
        commentRepository.deleteById(ID_3);
        assertThat(commentRepository.findById(ID_3)).isEmpty();
    }
}