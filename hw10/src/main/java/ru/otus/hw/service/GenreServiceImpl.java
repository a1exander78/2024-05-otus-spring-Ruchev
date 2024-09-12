package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.converter.dto.GenreDtoConverter;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.repository.GenreRepository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class GenreServiceImpl implements GenreService {
    private final GenreRepository genreRepository;

    private final GenreDtoConverter converter;

    @Override
    public List<GenreDto> findAll() {
        return genreRepository.findAll().stream().map(converter::toDto).toList();
    }

    @Override
    public Optional<GenreDto> findById(long id) {
        return genreRepository.findById(id).map(converter::toDto);
    }
}
