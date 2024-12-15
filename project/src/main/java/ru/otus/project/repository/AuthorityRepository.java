package ru.otus.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.project.model.Authority;

public interface AuthorityRepository extends JpaRepository<Authority, Long> {
}
