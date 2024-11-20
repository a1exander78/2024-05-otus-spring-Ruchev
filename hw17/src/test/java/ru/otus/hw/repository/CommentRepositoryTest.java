package ru.otus.hw.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.otus.hw.model.Book;
import ru.otus.hw.model.Comment;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе Jpa для работы с комментариями ")
@DataJpaTest
class CommentRepositoryTest {
    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private TestEntityManager em;

    @DisplayName("должен загружать список всех комментариев по книге")
    @Test
    void shouldReturnCorrectCommentsList() {
        var actualComments = commentRepository.findAllCommentsByBookId(1L);
        var expectedComment1 = em.find(Comment.class, 1L);
        var expectedComment2 = em.find(Comment.class, 4L);

        assertThat(actualComments).containsExactlyElementsOf(List.of(expectedComment1, expectedComment2));
        actualComments.forEach(System.out::println);
    }

    @DisplayName("должен загружать комментарий по id")
    @Test
    void shouldReturnCorrectCommentById() {
        var actualComment = commentRepository.findById(1L);
        var expectedComment = em.find(Comment.class, 1L);

        assertThat(actualComment).isPresent()
                .get()
                .isEqualTo(expectedComment);
    }

    @DisplayName("должен сохранять новый коментарий")
    @Test
    void shouldSaveNewComment() {
        String description = "newComment";

        var book = em.find(Book.class, 1L);
        var newComment = new Comment(0, description, book);
        var expectedComment = em.persist(newComment);
        var returnedComment = commentRepository.save(expectedComment);

        assertThat(returnedComment).isNotNull()
                .matches(comment -> comment.getId() > 0)
                .usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(expectedComment);

        assertThat(commentRepository.findById(returnedComment.getId()))
                .isPresent()
                .get()
                .isEqualTo(returnedComment);
    }

    @DisplayName("должен сохранять измененный комментарий")
    @Test
    void shouldSaveUpdatedComment() {
        String description = "editedComment";

        var book = em.find(Book.class, 1L);
        var expectedComment = new Comment(1L, description, book);

        assertThat(commentRepository.findById(expectedComment.getId()))
                .isPresent()
                .get()
                .isNotEqualTo(expectedComment);

        var returnedComment = commentRepository.save(expectedComment);
        assertThat(returnedComment).isNotNull()
                .matches(comment -> comment.getId() > 0)
                .usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(expectedComment);

        assertThat(commentRepository.findById(returnedComment.getId()))
                .isPresent()
                .get()
                .isEqualTo(returnedComment);
    }

    @DisplayName("должен удалять комментарий по id ")
    @Test
    void shouldDeleteComment() {
        assertThat(commentRepository.findById(1L)).isPresent();
        commentRepository.deleteById(1L);
        assertThat(commentRepository.findById(1L)).isEmpty();
    }
}