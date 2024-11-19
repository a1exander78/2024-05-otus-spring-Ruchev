package ru.otus.hw.repository.reactive;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;
import ru.otus.hw.model.Genre;

public interface GenreRepository extends ReactiveMongoRepository<Genre, ObjectId> {
    Mono<Genre> findById(ObjectId id);
}
