package ru.otus.hw.mappers;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Book;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.GenreRepository;

import java.sql.ResultSet;
import java.sql.SQLException;

@RequiredArgsConstructor
@Component
public class BookRowMapper implements RowMapper<Book> {

    private final AuthorRepository authorRepository;

    private final GenreRepository genreRepository;

    @Override
    public Book mapRow(ResultSet rs, int rowNum) throws SQLException {
        long id = rs.getLong("id");
        String title = rs.getString("title");
        var authorId = rs.getLong("author_id");
        var author = authorRepository.findById(authorId);
        var genreId = rs.getLong("genre_id");
        var genre = genreRepository.findById(genreId);

        String authorExceptionMessage = "Author with id %d not found".formatted(authorId);
        String genreExceptionMessage = "Genre with id %d not found".formatted(genreId);

        return new Book(id, title,
                author.orElseThrow(() -> new EntityNotFoundException(authorExceptionMessage)),
                genre.orElseThrow(() -> new EntityNotFoundException(genreExceptionMessage)));
    }
}
