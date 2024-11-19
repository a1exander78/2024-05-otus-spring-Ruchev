package ru.otus.hw.repository.reactive;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;
import ru.otus.hw.model.Author;

public interface AuthorRepository extends ReactiveMongoRepository<Author, ObjectId> {
    Mono<Author> findById(ObjectId id);
}
