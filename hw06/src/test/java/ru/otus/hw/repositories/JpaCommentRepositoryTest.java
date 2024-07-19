package ru.otus.hw.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.models.Genre;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе Jpa для работы с комментариями ")
@DataJpaTest
@Import(JpaCommentRepository.class)
class JpaCommentRepositoryTest {

    @Autowired
    private JpaCommentRepository jpaCommentRepository;

    @Autowired
    private TestEntityManager em;

    @DisplayName("должен загружать комментарий по id")
    @Test
    void shouldReturnCorrectCommentById() {
        var actualComment = jpaCommentRepository.findById(1L);
        var expectedComment = em.find(Comment.class, 1L);
        assertThat(actualComment).isPresent()
                .get()
                .isEqualTo(expectedComment);
    }

//    @DisplayName("должен загружать список всех комментариев по книге")
//    @Test
//    void shouldReturnCorrectBooksList() {
//
//
//        var actualComments = jpaCommentRepository.findAllCommentsByBook()
//
//        var expectedBook1 = em.find(Book.class, 1L);
//        var expectedBook2 = em.find(Book.class, 2L);
//        var expectedBook3 = em.find(Book.class, 3L);
//
//        assertThat(actualBooks).containsExactlyElementsOf(List.of(expectedBook1, expectedBook2, expectedBook3));
//        actualBooks.forEach(System.out::println);
//    }

    @DisplayName("должен сохранять новый коментарий")
    @Test
    void shouldSaveNewComment() {
        String description = "newComment";
        var book = em.find(Book.class, 1L);
        var newComment = new Comment(0, description, book);

        var expectedComment = em.persist(newComment);
        var returnedComment = jpaCommentRepository.save(expectedComment);
        assertThat(returnedComment).isNotNull()
                .matches(comment -> comment.getId() > 0)
                .usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(expectedComment);

        assertThat(jpaCommentRepository.findById(returnedComment.getId()))
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

        assertThat(jpaCommentRepository.findById(expectedComment.getId()))
                .isPresent()
                .get()
                .isNotEqualTo(expectedComment);

        var returnedComment = jpaCommentRepository.save(expectedComment);
        assertThat(returnedComment).isNotNull()
                .matches(comment -> comment.getId() > 0)
                .usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(expectedComment);

        assertThat(jpaCommentRepository.findById(returnedComment.getId()))
                .isPresent()
                .get()
                .isEqualTo(returnedComment);
    }

    @DisplayName("должен удалять комментарий по id ")
    @Test
    void shouldDeleteComment() {
        assertThat(jpaCommentRepository.findById(1L)).isPresent();
        jpaCommentRepository.deleteById(1L);
        assertThat(jpaCommentRepository.findById(1L)).isEmpty();
    }
}