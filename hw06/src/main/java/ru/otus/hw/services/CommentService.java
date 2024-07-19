package ru.otus.hw.services;

import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;

import java.util.List;
import java.util.Optional;

public interface CommentService {
    Optional<Comment> findById(long id);

    List<Comment> findAllCommentsByBook(Book book);

    Comment insert(String description, long bookId);

    Comment update(long id, String description);

    void deleteById(long id);
}
