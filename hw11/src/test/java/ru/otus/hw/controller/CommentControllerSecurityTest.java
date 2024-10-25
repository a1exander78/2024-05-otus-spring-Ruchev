package ru.otus.hw.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.CommentDto;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.security.SecurityConfig;
import ru.otus.hw.service.BookService;
import ru.otus.hw.service.CommentService;
import ru.otus.hw.utils.SecurityTestUtils;

import java.util.Optional;

import static java.util.Objects.nonNull;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Контроллер комментариев")
@Import(SecurityConfig.class)
@WebMvcTest(CommentController.class)
class CommentControllerSecurityTest extends SecurityTestUtils {
    private static final long ID_1 = 1L;
    private static final long ID_4 = 4L;
    private static final long ID_5 = 5L;

    private static final AuthorDto AUTHOR_1 = new AuthorDto(ID_1, "Author_Test_1");

    private static final GenreDto GENRE_1 = new GenreDto(ID_1, "Genre_Test_1");

    private static final BookDto BOOK_1 = new BookDto(ID_1, "Book_Test_1", AUTHOR_1, GENRE_1);

    private static final String UPDATING_COMMENT = "Updating_Comment";

    @Autowired
    private MockMvc mvc;

    @MockBean
    private CommentService commentService;

    @MockBean
    private BookService bookService;

    @DisplayName("с учетом наличия/отсутствия авторизации должен вернуть")
    @MethodSource("generateData4CommentController")
    @ParameterizedTest(name = "при {0} запросе на \"{1}\" для пользователя {2} статус {4}")
    void shouldReturnCorrectStatus(String method, String url, String userName, String[] roles, int status, boolean checkLoginRedirection) throws Exception {
        var request = method2RequestBuilder(method, url);
        if (nonNull(userName)) {
            request = request.with(user(userName).roles(roles));
        }

        // for FindById
        given(bookService.findById(ID_1)).willReturn(Optional.of(BOOK_1));

        // for Update
        var updatedComment = new CommentDto(ID_1, UPDATING_COMMENT, ID_1);
        given(commentService.update(ID_1, null)).willReturn(updatedComment);

        var resultActions = mvc.perform(request).andExpect(status().is(status));
        if (checkLoginRedirection) {
            if (userName != null && method.equals("post")) {
                if (url.equals("/comment/1") || url.equals("/comment/new?bookId=1")) {
                    resultActions.andExpect(redirectedUrlPattern("/comment?bookId=1"));
                } else if (url.equals("/comment/1/del")) {
                    resultActions.andExpect(redirectedUrlPattern("/book/**"));
                }
            } else {
                resultActions.andExpect(redirectedUrlPattern("**/login"));
            }
        }
    }
}