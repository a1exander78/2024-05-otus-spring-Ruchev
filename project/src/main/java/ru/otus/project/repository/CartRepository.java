package ru.otus.project.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.otus.project.model.Cart;
import ru.otus.project.model.Status;

import java.util.List;
import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {
    @EntityGraph(attributePaths = {"cartStatus", "bags"})
    List<Cart> findAll();

    @EntityGraph(attributePaths = {"cartStatus", "user", "bags"})
    Optional<Cart> findById(long id);

    @EntityGraph(attributePaths = {"cartStatus", "bags"})
    List<Cart> findByUserId(long userId);

    @EntityGraph(attributePaths = "user")
    List<Cart> findByCartStatusId(long statusId);

    @Modifying
    @Query("update Cart c set c.cartStatus = :cartStatus where c.id = :id")
    int updateStatus(@Param(value = "id") long id, @Param(value = "cartStatus") Status cartStatus);
}
