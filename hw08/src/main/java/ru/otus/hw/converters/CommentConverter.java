package ru.otus.hw.converters;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.hw.models.Comment;

@RequiredArgsConstructor
@Component
public class CommentConverter {
    public String commentToString(Comment comment) {
        return "Id: %d, description: %s, bookId: %d".formatted(
                comment.getId(),
                comment.getDescription(),
                comment.getBook().getId());
    }
}
