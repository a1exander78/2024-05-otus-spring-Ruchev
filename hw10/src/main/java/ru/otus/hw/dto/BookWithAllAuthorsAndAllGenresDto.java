package ru.otus.hw.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookWithAllAuthorsAndAllGenresDto {
    private BookDto book;

    private List<AuthorDto> authors;

    private List<GenreDto> genres;
}
