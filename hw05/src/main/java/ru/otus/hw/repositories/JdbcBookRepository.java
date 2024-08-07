package ru.otus.hw.repositories;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.mappers.BookRowMapper;
import ru.otus.hw.models.Book;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class JdbcBookRepository implements BookRepository {

    private final NamedParameterJdbcOperations jdbc;

    private final BookRowMapper bookRowMapper;

    @Override
    public Optional<Book> findById(long id) {
        final Map<String, Object> params = Collections.singletonMap("id", id);
        Book book;
        try {
            book = jdbc.queryForObject("select id, title, author_id, genre_id from books where id = :id",
                    params, bookRowMapper);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
        return Optional.ofNullable(book);
    }

    @Override
    public List<Book> findAll() {
        return jdbc.query("select id, title, author_id, genre_id from books", bookRowMapper);
    }

    @Override
    public Book save(Book book) {
        if (book.getId() == 0) {
            return insert(book);
        }
        return update(book);
    }

    @Override
    public void deleteById(long id) {
        Map<String, Object> params = Collections.singletonMap("id", id);
        var deleteResult = jdbc.update("delete from books where id = :id", params);
        if (deleteResult == 0) {
            throw new EntityNotFoundException("Book with id %d not found".formatted(id));
        }
    }

    private Book insert(Book book) {
        var keyHolder = new GeneratedKeyHolder();
        var params = new MapSqlParameterSource();
        params.addValues(Map.of(
                "title", book.getTitle(),
                "author_id", book.getAuthor().getId(),
                "genre_id", book.getGenre().getId()));

        jdbc.update("insert into books (title, author_id, genre_id) values (:title, :author_id, :genre_id)",
               params, keyHolder, new String[]{"id"});

        //noinspection DataFlowIssue
        book.setId(keyHolder.getKeyAs(Long.class));
        return book;
    }

    private Book update(Book book) {
        var params = new MapSqlParameterSource();
        params.addValues(Map.of(
                "id", book.getId(),
                "title", book.getTitle(),
                "author_id", book.getAuthor().getId(),
                "genre_id", book.getGenre().getId()));

        var updateResult = jdbc.update("update books set " +
                "title = :title, " +
                "author_id = :author_id, " +
                "genre_id = :genre_id " +
                "where id = :id", params);

        if (updateResult == 0) {
            throw new EntityNotFoundException("Book with id %d not found".formatted(book.getId()));
        }
        return book;
    }
}
