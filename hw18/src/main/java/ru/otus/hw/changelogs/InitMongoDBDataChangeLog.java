package ru.otus.hw.changelogs;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.mongodb.client.MongoDatabase;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import ru.otus.hw.model.Author;
import ru.otus.hw.model.Book;
import ru.otus.hw.model.Comment;
import ru.otus.hw.model.Genre;
import ru.otus.hw.repository.classic.AuthorInitDataRepository;
import ru.otus.hw.repository.classic.BookInitDataRepository;
import ru.otus.hw.repository.classic.CommentInitDataRepository;
import ru.otus.hw.repository.classic.GenreInitDataRepository;

import java.util.List;

@RequiredArgsConstructor
@ChangeLog(order = "001")
public class InitMongoDBDataChangeLog {
    private static final Author AUTHOR_1 = new Author(new ObjectId(), "Author_1");

    private static final Author AUTHOR_2 = new Author(new ObjectId(), "Author_2");

    private static final Author AUTHOR_3 = new Author(new ObjectId(), "Author_3");

    private static final Genre GENRE_1 = new Genre(new ObjectId(), "Genre_1");

    private static final Genre GENRE_2 = new Genre(new ObjectId(), "Genre_2");

    private static final Genre GENRE_3 = new Genre(new ObjectId(), "Genre_3");

    private static final Book BOOK_1 = new Book(new ObjectId(), "Book_1", AUTHOR_1, GENRE_1);

    private static final Book BOOK_2 = new Book(new ObjectId(), "Book_2", AUTHOR_2, GENRE_2);

    private static final Book BOOK_3 = new Book(new ObjectId(), "Book_3", AUTHOR_3, GENRE_3);

    private static final Comment COMMENT_1 = new Comment(new ObjectId(), "Comment_1", BOOK_1);

    private static final Comment COMMENT_2 = new Comment(new ObjectId(), "Comment_2", BOOK_2);

    private static final Comment COMMENT_3 = new Comment(new ObjectId(), "Comment_3", BOOK_3);

    @ChangeSet(order = "000", id = "dropDB", author = "a1exander78", runAlways = true)
    public void dropDB(MongoDatabase database) {
        database.drop();
    }

    @ChangeSet(order = "001", id = "initAuthors", author = "a1exander78", runAlways = true)
    public void initAuthors(AuthorInitDataRepository repository) {
        repository.saveAll(List.of(AUTHOR_1, AUTHOR_2, AUTHOR_3));
    }

    @ChangeSet(order = "002", id = "initGenres", author = "a1exander78", runAlways = true)
    public void initGenres(GenreInitDataRepository repository) {
        repository.saveAll(List.of(GENRE_1, GENRE_2, GENRE_3));
    }

    @ChangeSet(order = "003", id = "initBooks", author = "a1exander78", runAlways = true)
    public void initBooks(BookInitDataRepository repository) {
        repository.saveAll(List.of(BOOK_1, BOOK_2, BOOK_3));
    }

    @ChangeSet(order = "004", id = "initComments", author = "a1exander78", runAlways = true)
    public void initComments(CommentInitDataRepository repository) {
        repository.saveAll(List.of(COMMENT_1, COMMENT_2, COMMENT_3));
    }
}
