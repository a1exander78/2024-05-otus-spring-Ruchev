package ru.otus.hw.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.converter.dto.CommentDtoConverterImpl;
import ru.otus.hw.dto.CommentDto;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional(propagation = Propagation.NEVER)
@DisplayName("Сервис для работы с комментариями")
@Import({CommentServiceImpl.class, CommentDtoConverterImpl.class})
@DataJpaTest
public class CommentServiceTest {
    private static final long ID_1 = 1L;
    private static final long ID_2 = 2L;

    private static final CommentDto COMMENT_1 = new CommentDto(ID_1, "Comment_Test_1", ID_2, ID_2);
    private static final CommentDto COMMENT_2 = new CommentDto(ID_2, "Comment_Test_2", ID_2, ID_2);

    private static final String NEW_COMMENT = "New_Comment";;

    private static final String UPDATING_COMMENT = "Updating_Comment";

    @Autowired
    private CommentService commentService;

    @MockBean
    private AclServiceWrapperService aclServiceWrapperService;

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    @DisplayName("должен загружать список всех комментариев по id книги")
    @Test
    void shouldReturnCorrectCommentsList() {
        assertThat(commentService.findAllCommentsByBookId(ID_2)).containsExactlyElementsOf(List.of(COMMENT_1, COMMENT_2));
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
        assertThat(commentService.findAllCommentsByBookId(ID_2).size()).isEqualTo(2);
        commentService.insert(NEW_COMMENT, ID_2, ID_2);
        var comments = commentService.findAllCommentsByBookId(ID_2);
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
