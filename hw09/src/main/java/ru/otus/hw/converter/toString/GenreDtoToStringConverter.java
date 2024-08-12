package ru.otus.hw.converter.toString;

import org.springframework.stereotype.Component;
import ru.otus.hw.dto.GenreDto;

@Component
public class GenreDtoToStringConverter {
    public String genreDtoToString(GenreDto genreDto) {
        return "Id: %d, Name: %s".formatted(genreDto.getId(), genreDto.getName());
    }
}
