package ru.otus.hw.controller.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import ru.otus.hw.converter.dto.GenreDtoConverter;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.repository.reactive.GenreRepository;

@RequiredArgsConstructor
@RestController
public class GenreRestController {
    private final GenreRepository genreRepository;

    private final GenreDtoConverter converter;

    @GetMapping("api/v1/genre/")
    public Flux<GenreDto> readAllGenres() {
        return genreRepository.findAll().map(converter::toDto);
    }
}
