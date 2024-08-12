package ru.otus.hw.converter.toString;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.hw.dto.CommentDto;

@RequiredArgsConstructor
@Component
public class CommentDtoToStringConverter {
    private final BookDtoToStringConverter bookDtoToStringConverter;

    public String commentDtoToString(CommentDto commentDto) {
        return "Id: %d, description: %s, bookId: %d".formatted(
                commentDto.getId(),
                commentDto.getDescription(),
                commentDto.getBookId());
    }
}
