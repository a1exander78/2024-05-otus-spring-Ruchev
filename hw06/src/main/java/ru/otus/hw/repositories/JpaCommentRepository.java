package ru.otus.hw.repositories;

import jakarta.persistence.EntityGraph;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Repository;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Comment;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.springframework.data.jpa.repository.EntityGraph.EntityGraphType.FETCH;

@RequiredArgsConstructor
@Repository
public class JpaCommentRepository implements CommentRepository {

    @PersistenceContext
    private final EntityManager em;

    @Override
    public List<Comment> findAllCommentsByBookId(long bookId) {
        EntityGraph<?> commentEntityGraph = em.getEntityGraph("comment-book-entity-graph");
        TypedQuery<Comment> query = em.createQuery("select c from Comment c where c.book.id = :bookId", Comment.class);
        query.setParameter("bookId", bookId);
        query.setHint(FETCH.getKey(), commentEntityGraph);
        return query.getResultList();
    }

    @Override
    public Optional<Comment> findById(long id) {
        Comment comment;
        try {
            EntityGraph<?> entityGraph = em.getEntityGraph("comment-book-entity-graph");
            Map<String, Object> properties = Collections.singletonMap(FETCH.getKey(), entityGraph);
            comment = em.find(Comment.class, id, properties);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
        return Optional.ofNullable(comment);
    }

    @Override
    public Comment save(Comment comment) {
        if (comment.getId() == 0) {
            em.persist(comment);
            return comment;
        }
        return em.merge(comment);
    }

    @Override
    public void deleteById(long id) {
        var comment = findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Comment with id %d not found".formatted(id)));
        em.remove(comment);
    }
}
