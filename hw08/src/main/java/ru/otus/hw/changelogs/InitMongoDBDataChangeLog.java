package ru.otus.hw.changelogs;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.mongodb.client.MongoDatabase;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.models.Genre;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.CommentRepository;
import ru.otus.hw.repositories.GenreRepository;

import java.util.List;

@ChangeLog(order = "001")
public class InitMongoDBDataChangeLog {

    private static final long ID_1 = 1L;
    private static final long ID_2 = 2L;
    private static final long ID_3 = 3L;
    private static final long ID_4 = 4L;

    private static final Author AUTHOR_1 = new Author(ID_1, "Author_1");
    private static final Author AUTHOR_2 = new Author(ID_2, "Author_2");
    private static final Author AUTHOR_3 = new Author(ID_3, "Author_3");

    private static final Genre GENRE_1 = new Genre(ID_1, "Genre_1");
    private static final Genre GENRE_2 = new Genre(ID_2, "Genre_2");
    private static final Genre GENRE_3 = new Genre(ID_3, "Genre_3");

    private static final Book BOOK_1 = new Book(ID_1, "Book_1", AUTHOR_1, GENRE_1);
    private static final Book BOOK_2 = new Book(ID_2, "Book_2", AUTHOR_2, GENRE_2);
    private static final Book BOOK_3 = new Book(ID_3, "Book_3", AUTHOR_3, GENRE_3);

    private static final Comment COMMENT_1 = new Comment(ID_1, "Comment_1", BOOK_1);
    private static final Comment COMMENT_2 = new Comment(ID_2, "Comment_2", BOOK_2);
    private static final Comment COMMENT_3 = new Comment(ID_3, "Comment_3", BOOK_3);

    @ChangeSet(order = "000", id = "dropDB", author = "a1exander78", runAlways = true)
    public void dropDB(MongoDatabase database){
        database.drop();
    }

    @ChangeSet(order = "001", id = "initAuthors", author = "a1exander78", runAlways = true)
    public void initAuthors(AuthorRepository repository){
        repository.saveAll(List.of(AUTHOR_1, AUTHOR_2, AUTHOR_3));
    }

    @ChangeSet(order = "002", id = "initGenres", author = "a1exander78", runAlways = true)
    public void initGenres(GenreRepository repository){
        repository.saveAll(List.of(GENRE_1, GENRE_2, GENRE_3));
    }

    @ChangeSet(order = "003", id = "initBooks", author = "a1exander78", runAlways = true)
    public void initBooks(BookRepository repository){
        repository.saveAll(List.of(BOOK_1, BOOK_2, BOOK_3));
    }

    @ChangeSet(order = "004", id = "initComments", author = "a1exander78", runAlways = true)
    public void initComments(CommentRepository repository){
        repository.saveAll(List.of(COMMENT_1, COMMENT_2, COMMENT_3));
    }


}
