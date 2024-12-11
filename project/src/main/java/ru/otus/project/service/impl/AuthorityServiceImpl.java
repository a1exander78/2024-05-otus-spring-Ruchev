package ru.otus.project.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.project.dto.authority.AuthorityDto;
import ru.otus.project.mapper.AuthorityMapper;
import ru.otus.project.model.Authority;
import ru.otus.project.repository.AuthorityRepository;
import ru.otus.project.repository.UserRepository;
import ru.otus.project.service.AuthorityService;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class AuthorityServiceImpl implements AuthorityService {
    private final AuthorityRepository authorityRepository;

    private final UserRepository userRepository;

    private final AuthorityMapper mapper;

    @Override
    public List<AuthorityDto> findAll() {
        return authorityRepository.findAll().stream().map(mapper::toDto).toList();
    }

    @Override
    public Optional<AuthorityDto> findById(long id) {
        return authorityRepository.findById(id).map(mapper::toDto);
    }

    @Override
    public AuthorityDto insert(String authority) {
        var newAuthority = authorityRepository.save(new Authority(0, authority));
        return mapper.toDto(newAuthority);
    }

    @Transactional
    @Override
    public int delete(long id) {
        var users = userRepository.findByAuthoritiesIn(List.of(id));
        if (!users.isEmpty() || id == 1) {
            return 0;
        }
        authorityRepository.deleteById(id);
        return 1;
    }
}
