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
import ru.otus.hw.models.Comment;
import ru.otus.hw.models.Genre;
import ru.otus.hw.repositories.JpaAuthorRepository;
import ru.otus.hw.repositories.JpaBookRepository;
import ru.otus.hw.repositories.JpaCommentRepository;
import ru.otus.hw.repositories.JpaGenreRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

@Transactional(propagation = Propagation.NEVER)
@DisplayName("Сервис для работы с комментариями")
@DataJpaTest
@Import({CommentServiceImpl.class, JpaBookRepository.class, JpaCommentRepository.class})
public class CommentServiceTest {
    private static final long ID_1 = 1L;
    private static final long ID_4 = 4L;
    private static final long ID_5 = 5L;

    private static final Author AUTHOR_1 = new Author(ID_1, "Author_Test_1");

    private static final Genre GENRE_1 = new Genre(ID_1, "Genre_Test_1");

    private static final Book BOOK_1 = new Book(ID_1, "Book_Test_1", AUTHOR_1, GENRE_1);

    private static final Comment COMMENT_1 = new Comment(ID_1, "Comment_Test_1", BOOK_1);
    private static final Comment COMMENT_4 = new Comment(ID_4, "Comment_Test_4", BOOK_1);

    private static final Comment NEW_COMMENT = new Comment(ID_5, "New_Comment", BOOK_1);

    private static final String UPDATING_DESCRIPTION = "Updating_Description";

    @Autowired
    private CommentService commentService;

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    @DisplayName("должен загружать список всех комментариев по id книги")
    @Test
    void shouldReturnCorrectCommentsList() {
        assertThat(commentService.findAllCommentsByBookId(ID_1)).containsExactlyElementsOf(List.of(COMMENT_1, COMMENT_4));
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    @DisplayName("должен загружать комментарий по id")
    @Test
    void shouldReturnCorrectCommentById() {
        assertThat(commentService.findById(ID_1)).contains(COMMENT_1);
    }

    @DisplayName("должен сохранять новый комментарий")
    @Test
    void shouldSaveNewComment() {
        assertThat(commentService.findById(NEW_COMMENT.getId())).isEmpty();
        commentService.insert(NEW_COMMENT.getDescription(), NEW_COMMENT.getBook().getId());
        assertThat(commentService.findById(NEW_COMMENT.getId())).contains(NEW_COMMENT);
    }

    @DisplayName("должен сохранять измененный комментарий")
    @Test
    void shouldSaveUpdatedComment() {
        assertThat(commentService.findById(ID_1)).contains(COMMENT_1);
        commentService.update(ID_1, UPDATING_DESCRIPTION);
        assertThat(commentService.findById(ID_1).orElseGet(() -> COMMENT_1)).isNotEqualTo(COMMENT_1);
    }

    @DisplayName("должен удалять комментарий по id ")
    @Test
    void shouldDeleteComment() {
        assertThat(commentService.findById(ID_1)).contains(COMMENT_1);
        commentService.deleteById(ID_1);
        assertThat(commentService.findById(ID_1)).isEmpty();
    }
}
