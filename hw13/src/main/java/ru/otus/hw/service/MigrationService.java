package ru.otus.hw.service;

import org.springframework.stereotype.Service;
import ru.otus.hw.model.mongo.Author;
import ru.otus.hw.model.mongo.Book;
import ru.otus.hw.model.mongo.Comment;
import ru.otus.hw.model.mongo.Genre;

@Service
public class MigrationService {
    public Author migrateAuthor(ru.otus.hw.model.h2.Author author) {
        return new Author(String.valueOf(author.getId()), author.getFullName());
    }

    public Genre migrateGenre(ru.otus.hw.model.h2.Genre genre) {
        return new Genre(String.valueOf(genre.getId()), genre.getName());
    }

    public Book migrateBook(ru.otus.hw.model.h2.Book book) {
        String id = String.valueOf(book.getId());
        String title = book.getTitle();
        var author =  migrateAuthor(book.getAuthor());
        var genre = migrateGenre(book.getGenre());
        return new Book(id, title, author, genre);
    }

    public Comment migrateComment(ru.otus.hw.model.h2.Comment comment) {
        return new Comment(String.valueOf(comment.getId()), comment.getDescription(), migrateBook(comment.getBook()));
    }
}
