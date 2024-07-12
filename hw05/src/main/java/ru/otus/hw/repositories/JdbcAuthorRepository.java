package ru.otus.hw.repositories;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.Author;

import java.sql.ResultSet;
import java.sql.SQLException;
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
        var author = jdbc.queryForObject("select id, full_name from authors where id = :id", params, authorRowMapper);
        return Optional.ofNullable(author);
    }

    @Component
    private static class AuthorRowMapper implements RowMapper<Author> {

        @Override
        public Author mapRow(ResultSet rs, int i) throws SQLException {
            long id = rs.getLong("id");
            String fullName = rs.getString("full_name");
            return new Author(id, fullName);
        }
    }
}
