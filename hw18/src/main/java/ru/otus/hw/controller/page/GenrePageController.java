package ru.otus.hw.controller.page;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@RequiredArgsConstructor
@Controller
public class GenrePageController {
    @GetMapping("/genre/")
    public String readAllGenres() {
        return "allGenres";
    }
}
