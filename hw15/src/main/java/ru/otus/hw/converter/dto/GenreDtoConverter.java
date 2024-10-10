package ru.otus.hw.converter.dto;

import org.mapstruct.Mapper;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.model.Genre;

@Mapper(componentModel = "spring")
public interface GenreDtoConverter {
    GenreDto toDto(Genre genre);
}
