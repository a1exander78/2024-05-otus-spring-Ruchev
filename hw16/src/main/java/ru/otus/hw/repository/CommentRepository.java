package ru.otus.hw.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.hw.model.Comment;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllCommentsByBookId(long bookId);

    Optional<Comment> findById(long id);

    Comment save(Comment comment);

    void deleteById(long id);
}
