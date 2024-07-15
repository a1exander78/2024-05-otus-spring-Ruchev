package ru.otus.hw.repositories;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;
import ru.otus.hw.mappers.GenreRowMapper;
import ru.otus.hw.models.Genre;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class JdbcGenreRepository implements GenreRepository {

    private final NamedParameterJdbcOperations jdbc;

    private final GenreRowMapper genreRowMapper;

    @Override
    public List<Genre> findAll() {
        return jdbc.query("select id, name from genres", genreRowMapper);
    }

    @Override
    public Optional<Genre> findById(long id) {
        final Map<String, Object> params = Collections.singletonMap("id", id);
        Genre genre;
        try {
            genre = jdbc.queryForObject("select id, name from genres where id = :id",
                    params, genreRowMapper);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
        return Optional.ofNullable(genre);
    }
}
