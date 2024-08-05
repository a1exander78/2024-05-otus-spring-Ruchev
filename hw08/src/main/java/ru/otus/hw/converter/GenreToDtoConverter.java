package ru.otus.hw.converter;

import org.springframework.stereotype.Component;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.model.Genre;

@Component
public class GenreToDtoConverter {
    public GenreDto convert(Genre genre) {
        return new GenreDto(genre.getId(), genre.getName());
    }
}
