package ru.otus.hw.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import ru.otus.hw.model.Book;

import java.util.List;
import java.util.Optional;

@RepositoryRestResource(path = "book")
public interface BookRepository extends JpaRepository<Book, Long> {
    @EntityGraph(attributePaths = {"author", "genre"})
    List<Book> findAll();

    @RestResource(rel = "by id")
    @EntityGraph(attributePaths = {"author", "genre"})
    Optional<Book> findById(long id);

    Book save(Book book);

    void deleteById(long id);

    @Query("select count(distinct c.book.id) from Comment c where c.book.id in" +
            "(select c.book.id from Comment c group by c.book.id having count(*) > :excess)")
    int getCountOfBooksWithCommentsExcess(@Param("excess") int excess);
}
