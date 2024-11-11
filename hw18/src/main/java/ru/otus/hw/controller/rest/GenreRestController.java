package ru.otus.hw.controller.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.service.GenreService;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class GenreRestController {
    private final GenreService genreService;

    @GetMapping("api/v1/genre/")
    public List<GenreDto> readAllGenres() {
        return genreService.findAll();
    }
}
