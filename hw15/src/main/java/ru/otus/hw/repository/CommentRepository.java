package ru.otus.hw.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import ru.otus.hw.model.Comment;

import java.util.List;
import java.util.Optional;

@RepositoryRestResource(path = "comment")
public interface CommentRepository extends JpaRepository<Comment, Long> {
    @RestResource(rel = "by book")
    List<Comment> findAllCommentsByBookId(long bookId);

    @RestResource(rel = "by id")
    Optional<Comment> findById(long id);

    Comment save(Comment comment);

    void deleteById(long id);

    @Query("select 1 from Comment c group by c.book.id having count(*) > :excess")
    List<Integer> findBooksWithCommentsExcess(@Param("excess") int excess);
}
