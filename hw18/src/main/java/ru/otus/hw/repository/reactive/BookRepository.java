package ru.otus.hw.repository.reactive;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.model.Book;

public interface BookRepository extends ReactiveMongoRepository<Book, ObjectId> {
    Flux<Book> findAll();

    Mono<Book> findById(ObjectId id);

    Mono<Book> save(Book book);

    Mono<Void> deleteById(ObjectId id);
}
