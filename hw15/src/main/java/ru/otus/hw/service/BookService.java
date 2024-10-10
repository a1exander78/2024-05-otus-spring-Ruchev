package ru.otus.hw.service;

import ru.otus.hw.dto.BookDto;

import java.util.List;
import java.util.Optional;

public interface BookService {
    List<BookDto> findAll();

    Optional<BookDto> findById(long id);

    BookDto insert(String title, long authorId, long genreId);

    BookDto update(long id, String title, long authorId, long genreId);

    void deleteById(long id);

    int getCountOfBooksWithCommentsExcess(int excess);
}
