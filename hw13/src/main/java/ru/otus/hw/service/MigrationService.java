package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;
import ru.otus.hw.model.mongo.Author;
import ru.otus.hw.model.mongo.Book;
import ru.otus.hw.model.mongo.Comment;
import ru.otus.hw.model.mongo.Genre;

import java.util.concurrent.ConcurrentHashMap;

@RequiredArgsConstructor
@Service
public class MigrationService {
    private final ConcurrentHashMap<Long, Author> authors = new ConcurrentHashMap<>();

    private final ConcurrentHashMap<Long, Genre> genres = new ConcurrentHashMap<>();

    private final ConcurrentHashMap<Long, Book> books = new ConcurrentHashMap<>();

    public Author migrateAuthor(ru.otus.hw.model.h2.Author author) {
        long id = author.getId();
        if (authors.get(id) == null) {
            authors.put(id, new Author(new ObjectId(), author.getFullName()));
        }
        return authors.get(id);
    }

    public Genre migrateGenre(ru.otus.hw.model.h2.Genre genre) {
        long id = genre.getId();
        if (genres.get(id) == null) {
            genres.put(id, new Genre(new ObjectId(), genre.getName()));
        }
        return genres.get(id);
    }

    public Book migrateBook(ru.otus.hw.model.h2.Book book) {
        long id = book.getId();
        if (books.get(id) == null) {
            var objectId = new ObjectId();
            String title = book.getTitle();
            var author = migrateAuthor(book.getAuthor());
            var genre = migrateGenre(book.getGenre());
            books.put(id, new Book(objectId, title, author, genre));
        }
        return books.get(id);
    }

    public Comment migrateComment(ru.otus.hw.model.h2.Comment comment) {
        var id = new ObjectId();
        return new Comment(id, comment.getDescription(), migrateBook(comment.getBook()));
    }
}
