package ru.otus.hw.controller.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.service.AuthorService;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class AuthorRestController {
    private final AuthorService authorService;

    @GetMapping("api/v1/author/")
    public List<AuthorDto> readAllAuthors() {
        return authorService.findAll();
    }
}
