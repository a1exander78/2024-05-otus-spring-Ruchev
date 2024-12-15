package ru.otus.project.controller.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.project.dto.status.StatusDto;
import ru.otus.project.service.StatusService;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class StatusRestController {
    private final StatusService statusService;

    @GetMapping("api/v1/status/")
    public List<StatusDto> readAllStatuses() {
        return statusService.findAll();
    }
}
