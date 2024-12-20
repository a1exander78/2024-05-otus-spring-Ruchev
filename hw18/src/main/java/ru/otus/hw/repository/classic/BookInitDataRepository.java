package ru.otus.hw.repository.classic;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.hw.model.Book;

import java.util.List;
import java.util.Optional;

public interface BookInitDataRepository extends MongoRepository<Book, Long> {
    List<Book> findAll();

    Optional<Book> findById(ObjectId id);

    Book save(Book book);

    void deleteById(ObjectId id);
}
