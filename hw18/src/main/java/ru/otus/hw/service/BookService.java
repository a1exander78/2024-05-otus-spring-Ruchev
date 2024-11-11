package ru.otus.hw.service;

import org.bson.types.ObjectId;
import ru.otus.hw.dto.BookDto;

import java.util.List;
import java.util.Optional;

public interface BookService {
    List<BookDto> findAll();

    Optional<BookDto> findById(ObjectId id);

    BookDto insert(String title, ObjectId authorId, ObjectId genreId);

    BookDto update(ObjectId id, String title, ObjectId authorId, ObjectId genreId);

    void deleteById(ObjectId id);
}
