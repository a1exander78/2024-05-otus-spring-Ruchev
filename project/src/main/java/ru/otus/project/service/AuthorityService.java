package ru.otus.project.service;

import ru.otus.project.dto.authority.AuthorityDto;

import java.util.List;
import java.util.Optional;

public interface AuthorityService {
    List<AuthorityDto> findAll();

    Optional<AuthorityDto> findById(long id);

    AuthorityDto insert(String authority);

    int delete(long id);
}
