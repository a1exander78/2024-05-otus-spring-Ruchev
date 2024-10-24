package ru.otus.hw.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.otus.hw.service.GenreService;

@RequiredArgsConstructor
@Controller
public class GenreController {
    private final GenreService genreService;

    @GetMapping("/genre/")
    public String readAllGenres(Model model) {
        var genres = genreService.findAll();
        model.addAttribute("genres", genres);
        return "allGenres";
    }
}
