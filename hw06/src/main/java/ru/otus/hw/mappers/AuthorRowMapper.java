package ru.otus.hw.mappers;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.otus.hw.models.Author;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class AuthorRowMapper implements RowMapper<Author> {

    @Override
    public Author mapRow(ResultSet rs, int i) throws SQLException {
        long id = rs.getLong("id");
        String fullName = rs.getString("full_name");
        return new Author(id, fullName);
    }
}
