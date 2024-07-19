package ru.otus.hw.converters;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.hw.models.Book;

@RequiredArgsConstructor
@Component
public class BookConverter {

    public String bookToString(Book book) {
        return "Id: %d, title: %s, author_id: %s, genre_id: %s".formatted(
                book.getId(),
                book.getTitle(),
                book.getAuthor().getId(),
                book.getGenre().getId());
    }
}
