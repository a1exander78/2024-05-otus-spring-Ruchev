package ru.otus.hw.converter.toString;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.hw.dto.CommentDto;

@RequiredArgsConstructor
@Component
public class CommentDtoToStringConverter {
    private final BookDtoToStringConverter bookDtoToStringConverter;

    public String commentDtoToString(CommentDto commentDto) {
        return "Id: %s, description: %s, bookId: %s".formatted(
                commentDto.getId(),
                commentDto.getDescription(),
                bookDtoToStringConverter.bookDtoToString(commentDto.getBook()));
    }
}
