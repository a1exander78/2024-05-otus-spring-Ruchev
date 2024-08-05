package ru.otus.hw.converter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.hw.dto.CommentDto;
import ru.otus.hw.model.Comment;

@RequiredArgsConstructor
@Component
public class CommentToDtoConverter {
    private final BookToDtoConverter bookToDtoConverter;

    public CommentDto convert(Comment comment) {
        var bookDto = bookToDtoConverter.convert(comment.getBook());
        return new CommentDto(comment.getId(), comment.getDescription(), bookDto);
    }
}
