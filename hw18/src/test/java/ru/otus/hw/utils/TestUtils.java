package ru.otus.hw.utils;

import org.bson.types.ObjectId;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.CommentDto;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.model.Author;
import ru.otus.hw.model.Book;
import ru.otus.hw.model.Comment;
import ru.otus.hw.model.Genre;

public class TestUtils {
    public static final ObjectId ID_1 = new ObjectId();

    public static final ObjectId ID_2 = new ObjectId();

    public static final ObjectId ID_3 = new ObjectId();

    public static final ObjectId ID_4 = new ObjectId();

    public static final Author AUTHOR_1 = new Author(ID_1, "Author_Test_1");
    public static final Author AUTHOR_2 = new Author(ID_2, "Author_Test_2");
    public static final Author AUTHOR_3 = new Author(ID_3, "Author_Test_3");

    public static final Genre GENRE_1 = new Genre(ID_1, "Genre_Test_1");
    public static final Genre GENRE_2 = new Genre(ID_2, "Genre_Test_2");
    public static final Genre GENRE_3 = new Genre(ID_3, "Genre_Test_3");

    public static final Book BOOK_1 = new Book(ID_1, "Book_Test_1", AUTHOR_1, GENRE_1);
    public static final Book BOOK_2 = new Book(ID_2, "Book_Test_2", AUTHOR_2, GENRE_2);
    public static final Book BOOK_3 = new Book(ID_3, "Book_Test_3", AUTHOR_3, GENRE_3);

    public static final Book UPDATING_BOOK = new Book(ID_1, "Updating_Title", AUTHOR_1, GENRE_1);

    public static final BookDto BOOK_DTO_1 = new BookDto(ID_1, "Book_Test_1", new AuthorDto(ID_1, "Author_Test_1"), new GenreDto(ID_1, "Genre_Test_1"));
    public static final BookDto BOOK_DTO_2 = new BookDto(ID_2, "Book_Test_2", new AuthorDto(ID_2, "Author_Test_2"), new GenreDto(ID_2, "Genre_Test_2"));
    public static final BookDto BOOK_DTO_3 = new BookDto(ID_3, "Book_Test_3", new AuthorDto(ID_3, "Author_Test_3"), new GenreDto(ID_3, "Genre_Test_3"));

    public static final String NEW_BOOK_TITLE = "New_Book";

    public static final String UPDATING_BOOK_TITLE = "Updating_Title";

    public static final Comment COMMENT_1 = new Comment(ID_1, "Comment_Test_1", BOOK_1);
    public static final Comment COMMENT_4 = new Comment(ID_4, "Comment_Test_4", BOOK_1);

    public static final Comment UPDATING_COMMENT = new Comment(ID_1, "Updating_Comment", BOOK_1);

    public static final CommentDto COMMENT_DTO_1 = new CommentDto(ID_1, "Comment_Test_1", ID_1);
    public static final CommentDto COMMENT_DTO_4 = new CommentDto(ID_4, "Comment_Test_4", ID_1);

    public static final String NEW_COMMENT_DESCRIPTION = "New_Comment";

    public static final String UPDATING_COMMENT_DESCRIPTION = "Updating_Comment";
}
