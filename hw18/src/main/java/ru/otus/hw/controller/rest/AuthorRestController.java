package ru.otus.hw.controller.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import ru.otus.hw.converter.dto.AuthorDtoConverter;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.repository.reactive.AuthorRepository;

@RequiredArgsConstructor
@RestController
public class AuthorRestController {
    private final AuthorRepository authorRepository;

    private final AuthorDtoConverter converter;

    @GetMapping("api/v1/author/")
    public Flux<AuthorDto> readAllAuthors() {
        return authorRepository.findAll().map(converter::toDto);
    }
}
