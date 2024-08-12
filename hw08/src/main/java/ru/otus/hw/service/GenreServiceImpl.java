package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.converter.toDto.GenreToDtoConverter;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.repository.GenreRepository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class GenreServiceImpl implements GenreService {
    private final GenreRepository genreRepository;

    private final GenreToDtoConverter genreToDtoConverter;

    @Override
    public List<GenreDto> findAll() {
        return genreRepository.findAll().stream().map(genreToDtoConverter::convert).toList();
    }

    @Override
    public Optional<GenreDto> findById(String id) {
        return genreRepository.findById(id).map(genreToDtoConverter::convert);
    }
}
