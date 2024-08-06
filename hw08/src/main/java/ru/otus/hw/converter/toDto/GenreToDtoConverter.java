package ru.otus.hw.converter.toDto;

import org.mapstruct.Mapper;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.model.Genre;

@Mapper(componentModel = "spring")
public interface GenreToDtoConverter {
    GenreDto convert(Genre genre);
}
