package ru.otus.hw.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.hw.model.Comment;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends MongoRepository<Comment, Long> {
    List<Comment> findAllCommentsByBookId(ObjectId bookId);

    Optional<Comment> findById(ObjectId id);

    Comment save(Comment comment);

    void deleteById(ObjectId id);
}
