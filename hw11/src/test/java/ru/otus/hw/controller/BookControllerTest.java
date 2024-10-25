package ru.otus.hw.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.service.AuthorService;
import ru.otus.hw.service.BookService;
import ru.otus.hw.service.GenreService;

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

@DisplayName("Контроллер книг")
@WebMvcTest(controllers = BookController.class,
        excludeAutoConfiguration = SecurityAutoConfiguration.class)
class BookControllerTest {
    private static final long ID_1 = 1L;
    private static final long ID_2 = 2L;
    private static final long ID_3 = 3L;
    private static final long ID_4 = 4L;

    private static final AuthorDto AUTHOR_1 = new AuthorDto(ID_1, "Author_Test_1");
    private static final AuthorDto AUTHOR_2 = new AuthorDto(ID_2, "Author_Test_2");
    private static final AuthorDto AUTHOR_3 = new AuthorDto(ID_3, "Author_Test_3");

    private static final GenreDto GENRE_1 = new GenreDto(ID_1, "Genre_Test_1");
    private static final GenreDto GENRE_2 = new GenreDto(ID_2, "Genre_Test_2");
    private static final GenreDto GENRE_3 = new GenreDto(ID_3, "Genre_Test_3");

    private static final BookDto BOOK_1 = new BookDto(ID_1, "Book_Test_1", AUTHOR_1, GENRE_1);
    private static final BookDto BOOK_2 = new BookDto(ID_2, "Book_Test_2", AUTHOR_2, GENRE_2);
    private static final BookDto BOOK_3 = new BookDto(ID_3, "Book_Test_3", AUTHOR_3, GENRE_3);

    private static final String NEW_TITLE = "New_Book";

    private static final String UPDATING_TITLE = "Updating_Title";

    private static final String SHORT_TITLE = "";

    private static final String LONG_TITLE = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";

    @Autowired
    private MockMvc mvc;

    @MockBean
    private BookService bookService;

    @MockBean
    private AuthorService authorService;

    @MockBean
    private GenreService genreService;

    @MockBean
    private MainController mainController;

    @DisplayName("должен возвращать корректный список книг")
    @Test
    void shouldReturnCorrectBooksList() throws Exception {
        var books = List.of(BOOK_1, BOOK_2, BOOK_3);
        given(bookService.findAll()).willReturn(books);

        mvc.perform(get("/book/"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(model().attribute("books", books));
    }

    @DisplayName("должен возвращать книгу по айди")
    @Test
    void shouldReturnBookById() throws Exception {
        given(bookService.findById(ID_1)).willReturn(Optional.of(BOOK_1));

        mvc.perform(get("/book/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(model().attribute("book", BOOK_1));
    }

    @DisplayName("должен сохранять новую книгу")
    @Test
    void shouldSaveNewBook() throws Exception {
        var newBook = new BookDto(ID_4,NEW_TITLE, AUTHOR_1, GENRE_1);

        given(bookService.insert(NEW_TITLE, ID_1, ID_1)).willReturn(newBook);

        var requestParams = new LinkedMultiValueMap<String, String>();

        requestParams.add("title", NEW_TITLE);
        requestParams.add("authorId", String.valueOf(ID_1));
        requestParams.add("genreId", String.valueOf(ID_1));

        mvc.perform(post("/book/new").params(requestParams))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/book/"));

        verify(bookService, times(1)).insert(NEW_TITLE, ID_1, ID_1);
    }

    @DisplayName("должен обновлять книгу")
    @Test
    void shouldUpdateBook() throws Exception {
        var updatedBook = new BookDto(ID_1, UPDATING_TITLE, AUTHOR_2, GENRE_3);

        given(bookService.update(ID_1, UPDATING_TITLE, ID_2, ID_3)).willReturn(updatedBook);

        var requestParams = new LinkedMultiValueMap<String, String>();

        requestParams.add("id", String.valueOf(ID_1));
        requestParams.add("title", UPDATING_TITLE);
        requestParams.add("authorId", String.valueOf(ID_2));
        requestParams.add("genreId", String.valueOf(ID_3));

        mvc.perform(post("/book/" + ID_1).params(requestParams))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/book/"));

        verify(bookService, times(1)).update(ID_1, UPDATING_TITLE, ID_2, ID_3);
    }

    @DisplayName("должен удалять книгу")
    @Test
    void shouldDeleteBook() throws Exception {
        mvc.perform(post("/book/1/del"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/book/"));

        verify(bookService, times(1)).deleteById(ID_1);
    }

    @DisplayName("не должен сохранять новую книгу, если наименование пустое или больше 30 символов")
    @Test
    void shouldNotSaveShortOrTooLongTitleBook() throws Exception {
        var requestParams = new LinkedMultiValueMap<String, String>();

        requestParams.add("title", SHORT_TITLE);
        requestParams.add("authorId", String.valueOf(ID_1));
        requestParams.add("genreId", String.valueOf(ID_1));

        mvc.perform(post("/book/new").params(requestParams))
                .andExpect(status().isOk());

        verify(bookService, times(0)).insert(SHORT_TITLE, ID_1, ID_1);

        requestParams.add("title", LONG_TITLE);

        mvc.perform(post("/book/new").params(requestParams))
                .andExpect(status().isOk());

        verify(bookService, times(0)).insert(LONG_TITLE, ID_1, ID_1);
    }

    @DisplayName("не должен обновлять книгу, если наименование пустое или больше 30 символов")
    @Test
    void shouldNotUpdateShortOrTooLongTitleBook() throws Exception {
        var requestParams = new LinkedMultiValueMap<String, String>();

        requestParams.add("id", String.valueOf(ID_1));
        requestParams.add("title", SHORT_TITLE);
        requestParams.add("authorId", String.valueOf(ID_2));
        requestParams.add("genreId", String.valueOf(ID_3));

        mvc.perform(post("/book/" + ID_1).params(requestParams))
                .andExpect(status().isOk());

        verify(bookService, times(0)).update(ID_1, SHORT_TITLE, ID_2, ID_3);

        requestParams.add("title", LONG_TITLE);

        mvc.perform(post("/book/" + ID_1).params(requestParams))
                .andExpect(status().isOk());

        verify(bookService, times(0)).update(ID_1, SHORT_TITLE, ID_2, ID_3);
    }
}