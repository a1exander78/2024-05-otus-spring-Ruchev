package ru.otus.project.controller.rest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.project.dto.authority.AuthorityDto;
import ru.otus.project.exception.EntityNotFoundException;
import ru.otus.project.exception.EntityUnprocessableException;
import ru.otus.project.service.AuthorityService;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class AuthorityRestController {
    private final AuthorityService authorityService;

    @GetMapping("api/v1/authority/")
    public List<AuthorityDto> readAllAuthorities() {
        return authorityService.findAll();
    }

    @GetMapping("api/v1/authority/{authorityId}")
    public AuthorityDto readAuthority(@PathVariable("authorityId") long authorityId) {
        return authorityService.findById(authorityId).orElseThrow(
                () -> new EntityNotFoundException("Authority with id %d not found".formatted(authorityId)));
    }

    @PostMapping("/api/v1/authority/")
    public void addAuthority(@Valid @RequestBody AuthorityDto dto,
                        BindingResult bindingResult) {
        if (!bindingResult.hasErrors()) {
            authorityService.insert(dto.getAuthority());
        }
    }

    @DeleteMapping("/api/v1/authority/{authorityId}")
    public void deleteAuthority(@PathVariable("authorityId") long authorityId) {
        if (authorityService.delete(authorityId) != 1) {
            throw new EntityUnprocessableException(("Authority with id %d already has users").formatted(authorityId));
        }
    }
}
