package ru.otus.hw.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.hw.model.Genre;

import java.util.Optional;

public interface GenreRepository extends MongoRepository<Genre, Long> {
    Optional<Genre> findById(ObjectId id);
}
