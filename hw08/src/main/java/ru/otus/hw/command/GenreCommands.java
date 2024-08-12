package ru.otus.hw.command;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.hw.converter.toString.GenreDtoToStringConverter;
import ru.otus.hw.service.GenreService;

import java.util.stream.Collectors;

@RequiredArgsConstructor
@ShellComponent
public class GenreCommands {
    private final GenreService genreService;

    private final GenreDtoToStringConverter genreDtoToStringConverter;

    @ShellMethod(value = "Find all genres", key = "ag")
    public String findAllGenres() {
        return genreService.findAll().stream()
                .map(genreDtoToStringConverter::genreDtoToString)
                .collect(Collectors.joining("," + System.lineSeparator()));
    }

    @ShellMethod(value = "Find genre by id", key = "gbid")
    public String findGenreById(String id) {
        return genreService.findById(id)
                .map(genreDtoToStringConverter::genreDtoToString)
                .orElse("Genre with id %s not found".formatted(id));
    }
}
