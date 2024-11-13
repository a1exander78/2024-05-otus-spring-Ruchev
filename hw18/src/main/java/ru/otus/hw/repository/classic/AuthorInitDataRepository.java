package ru.otus.hw.repository.classic;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.hw.model.Author;

import java.util.Optional;

public interface AuthorInitDataRepository extends MongoRepository<Author, Long> {
    Optional<Author> findById(ObjectId id);
}
