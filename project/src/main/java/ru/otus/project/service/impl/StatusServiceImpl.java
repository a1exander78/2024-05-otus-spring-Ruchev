package ru.otus.project.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.project.dto.status.StatusDto;
import ru.otus.project.mapper.StatusMapper;
import ru.otus.project.repository.StatusRepository;
import ru.otus.project.service.StatusService;

import java.util.List;

@RequiredArgsConstructor
@Service
public class StatusServiceImpl implements StatusService {
    private final StatusRepository statusRepository;

    private final StatusMapper mapper;

    @Override
    public List<StatusDto> findAll() {
        return statusRepository.findAll().stream().map(mapper::toDto).toList();
    }
}
