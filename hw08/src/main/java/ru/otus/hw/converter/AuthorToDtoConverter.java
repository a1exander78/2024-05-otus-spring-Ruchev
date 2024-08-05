package ru.otus.hw.converter;

import org.mapstruct.Mapper;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.model.Author;

@Mapper(componentModel = "spring")
public interface AuthorToDtoConverter {
    AuthorDto convert(Author author);
}
