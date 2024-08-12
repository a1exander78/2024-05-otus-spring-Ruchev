package ru.otus.hw.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.hw.model.Book;

public interface BookRepository extends MongoRepository<Book, String> {
}
