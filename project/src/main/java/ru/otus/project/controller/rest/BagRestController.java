package ru.otus.project.controller.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.project.dto.bag.BagDto;
import ru.otus.project.service.BagService;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class BagRestController {
    private final BagService bagService;

    @GetMapping("api/v1/bag/")
    public List<BagDto> readAllBags() {
        return bagService.findAll();
    }
}
