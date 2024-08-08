package ru.otus.hw.converter.toDto;

import org.mapstruct.Mapper;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.model.Author;
import ru.otus.hw.model.Book;
import ru.otus.hw.model.Genre;

@Mapper(componentModel = "spring")
public interface BookToDtoConverter {
    BookDto convert(Book book);

    AuthorDto convertAuthor(Author author);

    GenreDto convertGenre(Genre genre);
}
