package ru.otus.hw.converter.toDto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.otus.hw.dto.CommentDto;
import ru.otus.hw.model.Comment;

@Mapper(componentModel = "spring")
public interface CommentToDtoConverter {
    @Mapping(source = "book.id", target = "bookId")
    CommentDto convert(Comment comment);
}
