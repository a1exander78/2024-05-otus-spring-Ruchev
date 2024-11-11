package ru.otus.hw.service;

import org.bson.types.ObjectId;
import ru.otus.hw.dto.AuthorDto;

import java.util.List;
import java.util.Optional;

public interface AuthorService {
    List<AuthorDto> findAll();

    Optional<AuthorDto> findById(ObjectId id);
}
