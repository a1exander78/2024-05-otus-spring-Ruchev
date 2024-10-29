package ru.otus.hw.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.otus.hw.service.AuthorService;

@RequiredArgsConstructor
@Controller
public class AuthorController {
    private final AuthorService authorService;

    @GetMapping("/author/")
    public String readAllAuthors(Model model) {
        var authors = authorService.findAll();
        model.addAttribute("authors", authors);
        return "allAuthors";
    }
}
