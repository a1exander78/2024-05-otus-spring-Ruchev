package ru.otus.hw.converter.toString;

import org.springframework.stereotype.Component;
import ru.otus.hw.dto.AuthorDto;

@Component
public class AuthorDtoToStringConverter {
    public String authorDtoToString(AuthorDto authorDto) {
        return "Id: %d, FullName: %s".formatted(authorDto.getId(), authorDto.getFullName());
    }
}
