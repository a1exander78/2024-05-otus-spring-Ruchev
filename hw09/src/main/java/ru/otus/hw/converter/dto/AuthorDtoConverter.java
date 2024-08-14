package ru.otus.hw.converter.dto;

import org.mapstruct.Mapper;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.model.Author;

@Mapper(componentModel = "spring")
public interface AuthorDtoConverter {
    AuthorDto toDto(Author author);
}
