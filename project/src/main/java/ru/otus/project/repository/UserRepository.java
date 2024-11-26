package ru.otus.project.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.otus.project.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findAll();

    @EntityGraph(attributePaths = "authorities")
    Optional<User> findById(long id);

    @EntityGraph(attributePaths = "authorities")
    Optional<User> findByLogin(String login);

    List<User> findByAddressStreetAndAddressStreetNumber(String street, String streetNumber);

    List<User> findByAddressDistrict(String district);

    @Modifying
    @Query("update User u set u.password = :password where u.id = :id")
    int updateUserPassword(@Param(value = "id") long id, @Param(value = "password") String password);

    @Modifying
    @Query("update User u set u.userName = :userName where u.id = :id")
    int updateUserName(@Param(value = "id") long id, @Param(value = "userName") String userName);
}
