package ru.otus.hw.service;

import ru.otus.hw.dto.BookDto;

import java.util.List;
import java.util.Optional;

public interface BookService {
    List<BookDto> findAll();

    Optional<BookDto> findById(String id);

    BookDto insert(String title, String authorId, String genreId);

    BookDto update(String id, String title, String authorId, String genreId);

    BookDto forceUpdate(String id, String title, String authorId, String genreId);

    void deleteById(String id);
}
