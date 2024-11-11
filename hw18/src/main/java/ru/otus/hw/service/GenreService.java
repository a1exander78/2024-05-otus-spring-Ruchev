package ru.otus.hw.service;

import org.bson.types.ObjectId;
import ru.otus.hw.dto.GenreDto;

import java.util.List;
import java.util.Optional;

public interface GenreService {
    List<GenreDto> findAll();

    Optional<GenreDto> findById(ObjectId id);
}
