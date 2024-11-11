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
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.BookDtoRequest;
import ru.otus.hw.service.AuthorService;
import ru.otus.hw.service.BookService;
import ru.otus.hw.service.GenreService;

import java.util.List;
import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.otus.hw.utils.TestUtils.ID_1;
import static ru.otus.hw.utils.TestUtils.ID_2;
import static ru.otus.hw.utils.TestUtils.ID_3;


@DisplayName("REST-контроллер книг")
@AutoConfigureDataMongo
@WebMvcTest(BookRestController.class)
class BookRestControllerTest {
    private static final AuthorDto AUTHOR_1 = new AuthorDto(ID_1, "Author_Test_1");
    private static final AuthorDto AUTHOR_2 = new AuthorDto(ID_2, "Author_Test_2");
    private static final AuthorDto AUTHOR_3 = new AuthorDto(ID_3, "Author_Test_3");

    private static final GenreDto GENRE_1 = new GenreDto(ID_1, "Genre_Test_1");
    private static final GenreDto GENRE_2 = new GenreDto(ID_2, "Genre_Test_2");
    private static final GenreDto GENRE_3 = new GenreDto(ID_3, "Genre_Test_3");

    private static final BookDto BOOK_1 = new BookDto(ID_1, "Book_Test_1", AUTHOR_1, GENRE_1);
    private static final BookDto BOOK_2 = new BookDto(ID_2, "Book_Test_2", AUTHOR_2, GENRE_2);
    private static final BookDto BOOK_3 = new BookDto(ID_3, "Book_Test_3", AUTHOR_3, GENRE_3);

    private static final List<AuthorDto> AUTHORS = List.of(AUTHOR_1, AUTHOR_2, AUTHOR_3);

    private static final List<GenreDto> GENRES = List.of(GENRE_1, GENRE_2, GENRE_3);

    private static final String NEW_TITLE = "New_Book";

    private static final String UPDATING_TITLE = "Updating_Title";

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private BookService bookService;

    @MockBean
    private AuthorService authorService;

    @MockBean
    private GenreService genreService;

    @DisplayName("должен возвращать корректный список книг")
    @Test
    void shouldReturnCorrectBooksList() throws Exception {
        var books = List.of(BOOK_1, BOOK_2, BOOK_3);
        given(bookService.findAll()).willReturn(books);

        mvc.perform(get("/api/v1/book/"))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(books)));
    }

    @DisplayName("должен возвращать книгу по айди")
    @Test
    void shouldReturnBookById() throws Exception {
        given(bookService.findById(ID_1)).willReturn(Optional.of(BOOK_1));
        given(authorService.findAll()).willReturn(AUTHORS);
        given(genreService.findAll()).willReturn(GENRES);

        mvc.perform(get("/api/v1/book/" + ID_1))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(BOOK_1)));
    }

    @DisplayName("должен обновлять книгу")
    @Test
    void shouldUpdateBook() throws Exception {
        var updatedBook = new BookDtoRequest(ID_1, UPDATING_TITLE, ID_1, ID_1);
        var expectedResult = mapper.writeValueAsString(updatedBook);

        mvc.perform(put("/api/v1/book/" + ID_1).contentType(APPLICATION_JSON)
                .content(expectedResult))
                .andExpect(status().isOk());

        verify(bookService, times(1)).update(ID_1, UPDATING_TITLE, ID_1, ID_1);
    }

    @DisplayName("должен сохранять новую книгу")
    @Test
    void shouldSaveNewBook() throws Exception {
        var newBook = new BookDtoRequest(new ObjectId(),NEW_TITLE, ID_1, ID_1);
        var expectedResult = mapper.writeValueAsString(newBook);

        mvc.perform(post("/api/v1/book").contentType(APPLICATION_JSON)
                .content(expectedResult))
                .andExpect(status().isOk());

        verify(bookService, times(1)).insert(NEW_TITLE, ID_1, ID_1);
    }

    @DisplayName("должен удалять книгу")
    @Test
    void shouldDeleteBook() throws Exception {
        mvc.perform(delete("/api/v1/book/"  + ID_1))
                .andExpect(status().isOk());

        verify(bookService, times(1)).deleteById(ID_1);
    }
}