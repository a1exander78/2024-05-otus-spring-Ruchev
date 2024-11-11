package ru.otus.hw.controller;

import org.bson.types.ObjectId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import ru.otus.hw.controller.page.CommentPageController;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static ru.otus.hw.utils.TestUtils.ID_1;
import static ru.otus.hw.utils.TestUtils.ID_4;

@DisplayName("Контроллер комментариев")
@AutoConfigureDataMongo
@WebMvcTest(CommentPageController.class)
class CommentControllerTest {
    private static final AuthorDto AUTHOR_1 = new AuthorDto(ID_1, "Author_Test_1");

    private static final GenreDto GENRE_1 = new GenreDto(ID_1, "Genre_Test_1");

    private static final BookDto BOOK_1 = new BookDto(ID_1, "Book_Test_1", AUTHOR_1, GENRE_1);

    private static final CommentDto COMMENT_1 = new CommentDto(ID_1, "Comment_Test_1", ID_1);
    private static final CommentDto COMMENT_4 = new CommentDto(ID_4, "Comment_Test_4", ID_1);

    private static final String NEW_COMMENT = "New_Comment";

    private static final String UPDATING_COMMENT = "Updating_Comment";

    private static final String SHORT_COMMENT = "ABCD";

    @Autowired
    private MockMvc mvc;

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

        mvc.perform(get("/comment?bookId=" + ID_1))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(model().attribute("comments", comments))
                .andExpect(model().attribute("book", BOOK_1));
    }

    @DisplayName("должен возвращать комментарий по айди")
    @Test
    void shouldReturnCommentById() throws Exception {
        given(commentService.findById(ID_1)).willReturn(Optional.of(COMMENT_1));

        mvc.perform(get("/comment/" + ID_1))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(model().attribute("comment", COMMENT_1));
    }
//
    @DisplayName("должен сохранять новый комментарий")
    @Test
    void shouldSaveNewComment() throws Exception {
        var newComment = new CommentDto(new ObjectId(), NEW_COMMENT, ID_1);

        given(commentService.insert(NEW_COMMENT, ID_1)).willReturn(newComment);

        var requestParams = new LinkedMultiValueMap<String, String>();

        requestParams.add("description", NEW_COMMENT);
        requestParams.add("bookId", String.valueOf(ID_1));

        mvc.perform(post("/comment/new").params(requestParams))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/comment?bookId=" + ID_1));

        verify(commentService, times(1)).insert(NEW_COMMENT, ID_1);
    }

    @DisplayName("должен обновлять комментарий")
    @Test
    void shouldUpdateComment() throws Exception {
        var updatedComment = new CommentDto(ID_1, UPDATING_COMMENT, ID_1);

        given(commentService.update(ID_1, UPDATING_COMMENT)).willReturn(updatedComment);

        var requestParams = new LinkedMultiValueMap<String, String>();

        requestParams.add("id", String.valueOf(ID_1));
        requestParams.add("description", UPDATING_COMMENT);

        mvc.perform(post("/comment/" + ID_1).params(requestParams))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/comment?bookId=" + ID_1));

        verify(commentService, times(1)).update(ID_1, UPDATING_COMMENT);
    }

    @DisplayName("должен удалять комментарий")
    @Test
    void shouldDeleteComment() throws Exception {
        mvc.perform(post("/comment/"+ ID_1 + "/del"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/book/"));

        verify(commentService, times(1)).deleteById(ID_1);
    }

    @DisplayName("не должен добавлять комментарий, длиной меньше 5 символов")
    @Test
    void shouldNotSaveShortComment() throws Exception {
        var requestParams = new LinkedMultiValueMap<String, String>();

        requestParams.add("description", SHORT_COMMENT);
        requestParams.add("bookId", String.valueOf(ID_1));

        mvc.perform(post("/comment/new").params(requestParams))
                .andExpect(status().isOk());

        verify(commentService, times(0)).insert(SHORT_COMMENT, ID_1);
    }

    @DisplayName("не должен обновлять комментарий длиной меньше 5 символов")
    @Test
    void shouldNotUpdateShortComment() throws Exception {
        var requestParams = new LinkedMultiValueMap<String, String>();

        requestParams.add("id", String.valueOf(ID_1));
        requestParams.add("description", SHORT_COMMENT);

        given(commentService.findById(ID_1)).willReturn(Optional.of(COMMENT_1));

        mvc.perform(post("/comment/"+ ID_1).params(requestParams))
                .andExpect(status().isOk());

        verify(commentService, times(0)).update(ID_1, SHORT_COMMENT);
    }
}