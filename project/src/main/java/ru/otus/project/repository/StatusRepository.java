package ru.otus.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.project.model.Status;

public interface StatusRepository extends JpaRepository<Status, Long> {
}
