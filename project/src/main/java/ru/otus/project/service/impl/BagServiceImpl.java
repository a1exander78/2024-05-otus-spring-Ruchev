package ru.otus.project.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.project.dto.bag.BagDto;
import ru.otus.project.mapper.BagMapper;
import ru.otus.project.repository.BagRepository;
import ru.otus.project.service.BagService;

import java.util.List;

@RequiredArgsConstructor
@Service
public class BagServiceImpl implements BagService {
    private final BagRepository bagRepository;

    private final BagMapper mapper;

    @Override
    public List<BagDto> findAll() {
        return bagRepository.findAll().stream().map(mapper::toDto).toList();
    }
}
