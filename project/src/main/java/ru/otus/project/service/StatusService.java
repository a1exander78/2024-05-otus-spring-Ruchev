package ru.otus.project.service;

import ru.otus.project.dto.status.StatusDto;

import java.util.List;

public interface StatusService {
    List<StatusDto> findAll();
}
