package ru.otus.hw.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.converter.dto.CommentDtoConverterImpl;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.CommentDto;
import ru.otus.hw.dto.GenreDto;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional(propagation = Propagation.NEVER)
@DisplayName("Сервис для работы с комментариями")
@Import({CommentServiceImpl.class, CommentDtoConverterImpl.class})
@DataJpaTest
public class CommentServiceTest {
    private static final long ID_1 = 1L;
    private static final long ID_4 = 4L;

    private static final AuthorDto AUTHOR_1 = new AuthorDto(ID_1, "Author_Test_1");

    private static final GenreDto GENRE_1 = new GenreDto(ID_1, "Genre_Test_1");

    private static final BookDto BOOK_1 = new BookDto(ID_1, "Book_Test_1", AUTHOR_1, GENRE_1);

    private static final CommentDto COMMENT_1 = new CommentDto(ID_1, "Comment_Test_1", ID_1);
    private static final CommentDto COMMENT_4 = new CommentDto(ID_4, "Comment_Test_4", ID_1);

    private static final String NEW_COMMENT = "New_Comment";;

    private static final String UPDATING_COMMENT = "Updating_Comment";

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
        assertThat(commentService.findAllCommentsByBookId(ID_1).size()).isEqualTo(2);
        commentService.insert(NEW_COMMENT, ID_1);
        var comments = commentService.findAllCommentsByBookId(ID_1);
        int size = comments.size();
        assertThat(size).isEqualTo(3);
        var newComment = comments.get(size - 1);
        assertThat(newComment.getDescription().equals(NEW_COMMENT));
    }

    @DisplayName("должен сохранять измененный комментарий")
    @Test
    void shouldSaveUpdatedComment() {
        assertThat(commentService.findById(ID_1)).contains(COMMENT_1);
        commentService.update(ID_1, UPDATING_COMMENT);
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
