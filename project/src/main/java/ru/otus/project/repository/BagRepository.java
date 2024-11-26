package ru.otus.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.project.model.Bag;

public interface BagRepository extends JpaRepository<Bag, Long> {
}
