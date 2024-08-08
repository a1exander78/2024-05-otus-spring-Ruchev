package ru.otus.hw.converter.toString;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.exception.EntityNotFoundException;

import static java.util.Objects.isNull;

@RequiredArgsConstructor
@Component
public class BookDtoToStringConverter {
    private final AuthorDtoToStringConverter authorDtoToStringConverter;

    private final GenreDtoToStringConverter genreDtoToStringConverter;

    public String bookDtoToString(BookDto bookDto) {
        if (isNull(bookDto)) {
            throw new EntityNotFoundException("Book not found");
        }
        return "Id: %s, title: %s, author: [%s], genre: [%s]".formatted(
                bookDto.getId(),
                bookDto.getTitle(),
                authorDtoToStringConverter.authorDtoToString(bookDto.getAuthor()),
                genreDtoToStringConverter.genreDtoToString(bookDto.getGenre()));
    }
}
