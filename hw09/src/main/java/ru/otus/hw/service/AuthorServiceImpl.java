package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.converter.dto.AuthorDtoConverter;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.repository.AuthorRepository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class AuthorServiceImpl implements AuthorService {
    private final AuthorRepository authorRepository;

    private final AuthorDtoConverter converter;

    @Override
    public List<AuthorDto> findAll() {
        return authorRepository.findAll().stream().map(converter::toDto).toList();
    }

    @Override
    public Optional<AuthorDto> findById(long id) {
        return authorRepository.findById(id).map(converter::toDto);
    }
}
