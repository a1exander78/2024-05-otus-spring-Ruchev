package ru.otus.project.service;

import ru.otus.project.dto.bag.BagDto;

import java.util.List;

public interface BagService {
    List<BagDto> findAll();
}
