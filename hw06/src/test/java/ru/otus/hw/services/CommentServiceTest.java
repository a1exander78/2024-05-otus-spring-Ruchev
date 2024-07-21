package ru.otus.hw.services;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.models.Comment;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.CommentRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@Transactional(propagation = Propagation.NEVER)
@DisplayName("Сервис для работы с комментариями")
@DataJpaTest
@Import(CommentServiceImpl.class)
public class CommentServiceTest {

    @MockBean
    private CommentRepository commentRepository;

    @MockBean
    private BookRepository bookRepository;

    @Autowired
    private CommentService commentService;

    @DisplayName("должен загружать комментарий по id")
    @Test
    void shouldReturnCorrectCommentById() {
        var expectedComment = new Comment(1L, "ExpectedComment", null);
        long expectedCommentId = expectedComment.getId();
        given(commentRepository.findById(expectedCommentId)).willReturn(Optional.of(expectedComment));
        assertThat(commentService.findById(expectedCommentId)).contains(expectedComment);
    }
}
