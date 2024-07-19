package ru.otus.hw.repositories;

import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;

import java.util.List;
import java.util.Optional;

public interface CommentRepository {
    Optional<Comment> findById(long id);

    List<Comment> findAllCommentsByBook(Book book);

    Comment save(Comment comment);

    void deleteById(long id);
}
