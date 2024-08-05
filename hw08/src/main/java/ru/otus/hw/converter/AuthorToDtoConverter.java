package ru.otus.hw.converter;

import org.springframework.stereotype.Component;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.model.Author;

@Component
public class AuthorToDtoConverter {
    public AuthorDto convert(Author author) {
        return new AuthorDto(author.getId(), author.getFullName());
    }
}
