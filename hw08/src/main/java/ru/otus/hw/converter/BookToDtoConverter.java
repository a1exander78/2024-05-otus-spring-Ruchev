package ru.otus.hw.converter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.model.Book;

@RequiredArgsConstructor
@Component
public class BookToDtoConverter {
    private final AuthorToDtoConverter authorToDtoConverter;

    private final GenreToDtoConverter genreToDtoConverter;

    public BookDto convert(Book book) {
        var authorDto = authorToDtoConverter.convert(book.getAuthor());
        var genreDto = genreToDtoConverter.convert(book.getGenre());
        return new BookDto(book.getId(), book.getTitle(), authorDto, genreDto);
    }
}
