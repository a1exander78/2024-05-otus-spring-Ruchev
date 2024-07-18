package ru.otus.hw.repositories;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;
import ru.otus.hw.mappers.AuthorRowMapper;
import ru.otus.hw.models.Author;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@RequiredArgsConstructor
@Repository
public class JdbcAuthorRepository implements AuthorRepository {

    private final NamedParameterJdbcOperations jdbc;

    private final AuthorRowMapper authorRowMapper;

    @Override
    public List<Author> findAll() {
        return jdbc.query("select id, full_name from authors", authorRowMapper);
    }

    @Override
    public Optional<Author> findById(long id) {
        final Map<String, Object> params = Collections.singletonMap("id", id);
        Author author;
        try {
            author = jdbc.queryForObject("select id, full_name from authors where id = :id",
                    params, authorRowMapper);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
        return Optional.ofNullable(author);
    }
}
