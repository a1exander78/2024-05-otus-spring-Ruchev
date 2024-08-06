package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.converter.toDto.AuthorToDtoConverter;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.repository.AuthorRepository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class AuthorServiceImpl implements AuthorService {
    private final AuthorRepository authorRepository;

    private final AuthorToDtoConverter authorToDtoConverter;

    @Override
    public List<AuthorDto> findAll() {
        return  authorRepository.findAll().stream().map(authorToDtoConverter::convert).toList();
    }

    @Override
    public Optional<AuthorDto> findById(String id) {
        return authorRepository.findById(id).map(authorToDtoConverter::convert);
    }
}
