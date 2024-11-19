package ru.otus.hw.repository.classic;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.hw.model.Genre;

import java.util.Optional;

public interface GenreInitDataRepository extends MongoRepository<Genre, Long> {
    Optional<Genre> findById(ObjectId id);
}
