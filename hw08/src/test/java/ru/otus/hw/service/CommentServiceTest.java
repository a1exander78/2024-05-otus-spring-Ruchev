package ru.otus.hw.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.converter.*;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.CommentDto;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.exception.EntityNotFoundException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Transactional(propagation = Propagation.NEVER)
@Import({CommentServiceImpl.class, BookServiceImpl.class, AuthorServiceImpl.class, GenreServiceImpl.class,
        CommentToDtoConverterImpl.class, BookToDtoConverterImpl.class, AuthorToDtoConverterImpl.class, GenreToDtoConverterImpl.class})
@DisplayName("Сервис для работы с комментариями")
@DataMongoTest
public class CommentServiceTest {
    private static final String ID_1 = "1";
    private static final String ID_3 = "3";
    private static final String ID_4 = "4";

    private static final AuthorDto AUTHOR_1 = new AuthorDto(ID_1, "Author_Test_1");

    private static final GenreDto GENRE_1 = new GenreDto(ID_1, "Genre_Test_1");

    private static final BookDto BOOK_1 = new BookDto(ID_1, "Book_Test_1", AUTHOR_1, GENRE_1);

    private static final CommentDto COMMENT_1 = new CommentDto(ID_1, "Comment_Test_1", BOOK_1);
    private static final CommentDto COMMENT_4 = new CommentDto(ID_4, "Comment_Test_4", BOOK_1);

    private static final String NEW_COMMENT = "New_Comment";;

    private static final String UPDATING_COMMENT = "Updating_Comment";

    @Autowired
    private CommentService commentService;

    @Autowired
    private BookService bookService;

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

    @DisplayName("должен удалять комментарий по id")
    @Test
    void shouldDeleteComment() {
        assertThat(commentService.findById(ID_1)).contains(COMMENT_1);
        commentService.deleteById(ID_1);
        assertThat(commentService.findById(ID_1)).isEmpty();
    }

    @DisplayName("должен бросить исключение при попытке загрузки комментариев по удаленной книге")
    @Test
    void shouldDeleteCommentsIfBookIsDeleted() {
        bookService.deleteById(ID_3);
        assertThatThrownBy(() -> commentService.findAllCommentsByBookId(ID_3)).isInstanceOf(EntityNotFoundException.class);
    }
}
