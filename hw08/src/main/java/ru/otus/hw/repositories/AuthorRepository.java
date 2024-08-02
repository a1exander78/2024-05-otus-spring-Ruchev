package ru.otus.hw.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.hw.models.Author;

import java.util.Optional;

public interface AuthorRepository extends MongoRepository<Author, Long> {
    Optional<Author> findById(long id);
}
