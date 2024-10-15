package ru.otus.hw.converter.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.otus.hw.dto.CommentDto;
import ru.otus.hw.model.Comment;

@Mapper(componentModel = "spring")
public interface CommentDtoConverter {
    @Mapping(source = "book.id", target = "bookId")
    CommentDto toDto(Comment comment);
}
