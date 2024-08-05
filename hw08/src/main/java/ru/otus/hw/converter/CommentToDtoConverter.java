package ru.otus.hw.converter;

import org.mapstruct.Mapper;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.CommentDto;
import ru.otus.hw.model.Book;
import ru.otus.hw.model.Comment;

@Mapper(componentModel = "spring")
public interface CommentToDtoConverter {
    CommentDto convert(Comment comment);

    BookDto convertBook(Book book);
}
