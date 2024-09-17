package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.converter.LongToObjectIdConverter;
import ru.otus.hw.model.mongo.Author;
import ru.otus.hw.model.mongo.Book;
import ru.otus.hw.model.mongo.Comment;
import ru.otus.hw.model.mongo.Genre;

@RequiredArgsConstructor
@Service
public class MigrationService {
    private final LongToObjectIdConverter converter1;

    public Author migrateAuthor(ru.otus.hw.model.h2.Author author) {
        var id = converter1.convertLongToObjectId(author.getId());
        return new Author(id, author.getFullName());
    }

    public Genre migrateGenre(ru.otus.hw.model.h2.Genre genre) {
        var id = converter1.convertLongToObjectId(genre.getId());
        return new Genre(id, genre.getName());
    }

    public Book migrateBook(ru.otus.hw.model.h2.Book book) {
        var id = converter1.convertLongToObjectId(book.getId());
        String title = book.getTitle();
        var author =  migrateAuthor(book.getAuthor());
        var genre = migrateGenre(book.getGenre());
        return new Book(id, title, author, genre);
    }

    public Comment migrateComment(ru.otus.hw.model.h2.Comment comment) {
        var id = converter1.convertLongToObjectId(comment.getId());
        return new Comment(id, comment.getDescription(), migrateBook(comment.getBook()));
    }
}
