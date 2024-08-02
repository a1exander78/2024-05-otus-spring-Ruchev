package ru.otus.hw.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.hw.models.Comment;

public interface CommentRepository extends MongoRepository<Comment, Long> {
}
