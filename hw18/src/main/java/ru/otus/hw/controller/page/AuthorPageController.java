package ru.otus.hw.controller.page;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@RequiredArgsConstructor
@Controller
public class AuthorPageController {
    @GetMapping("/author/")
    public String readAllAuthors() {
        return "allAuthors";
    }
}
