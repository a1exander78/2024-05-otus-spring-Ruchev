package ru.otus.hw.controller.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.CommentDto;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.service.BookService;
import ru.otus.hw.service.CommentService;

import java.util.List;
import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.otus.hw.utils.TestUtils.ID_1;
import static ru.otus.hw.utils.TestUtils.ID_4;


@DisplayName("REST-контроллер комментариев")
@AutoConfigureDataMongo
@WebMvcTest(CommentRestController.class)
class CommentRestControllerTest {
    private static final AuthorDto AUTHOR_1 = new AuthorDto(ID_1, "Author_Test_1");

    private static final GenreDto GENRE_1 = new GenreDto(ID_1, "Genre_Test_1");

    private static final BookDto BOOK_1 = new BookDto(ID_1, "Book_Test_1", AUTHOR_1, GENRE_1);

    private static final CommentDto COMMENT_1 = new CommentDto(ID_1, "Comment_Test_1", ID_1);
    private static final CommentDto COMMENT_4 = new CommentDto(ID_4, "Comment_Test_4", ID_1);

    private static final String NEW_COMMENT = "New_Comment";

    private static final String UPDATING_COMMENT = "Updating_Comment";

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private CommentService commentService;

    @MockBean
    private BookService bookService;

    @DisplayName("должен возвращать корректный список комментариев по айди книги")
    @Test
    void shouldReturnCorrectCommentsListByBookId() throws Exception {
        var comments = List.of(COMMENT_1, COMMENT_4);
        given(commentService.findAllCommentsByBookId(ID_1)).willReturn(comments);
        given(bookService.findById(ID_1)).willReturn(Optional.of(BOOK_1));

        mvc.perform(get("/api/v1/comment?bookId=" + ID_1))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(comments)));
    }

    @DisplayName("должен возвращать комментарий по айди")
    @Test
    void shouldReturnCommentById() throws Exception {
        given(commentService.findById(ID_1)).willReturn(Optional.of(COMMENT_1));

        mvc.perform(get("/api/v1/comment/" + ID_1))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(COMMENT_1)));
    }

    @DisplayName("должен сохранять новый комментарий")
    @Test
    void shouldSaveNewComment() throws Exception {
        var newComment = new CommentDto(new ObjectId(), NEW_COMMENT, ID_1);
        var expectedResult = mapper.writeValueAsString(newComment);

        mvc.perform(post("/api/v1/comment").contentType(APPLICATION_JSON)
                .content(expectedResult))
                .andExpect(status().isOk());

        verify(commentService, times(1)).insert(NEW_COMMENT, ID_1);
    }

    @DisplayName("должен обновлять комментарий")
    @Test
    void shouldUpdateComment() throws Exception {
        var updatedComment = new CommentDto(ID_1, UPDATING_COMMENT, ID_1);
        var expectedResult = mapper.writeValueAsString(updatedComment);

        mvc.perform(put("/api/v1/comment/" + ID_1).contentType(APPLICATION_JSON)
                .content(expectedResult))
                .andExpect(status().isOk());

        verify(commentService, times(1)).update(ID_1, UPDATING_COMMENT);
    }

    @DisplayName("должен удалять комментарий")
    @Test
    void shouldDeleteComment() throws Exception {
        mvc.perform(delete("/api/v1/comment/"+ ID_1))
                .andExpect(status().isOk());

        verify(commentService, times(1)).deleteById(ID_1);
    }
}