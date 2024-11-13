package ru.otus.hw.repository.reactive;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.model.Comment;

public interface CommentRepository extends ReactiveMongoRepository<Comment, ObjectId> {
    Flux<Comment> findAllCommentsByBookId(ObjectId bookId);

    Mono<Comment> findById(ObjectId id);

    Mono<Comment> save(Comment comment);

    Mono<Void> deleteById(ObjectId id);
}
