package ru.otus.project.controller.rest;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.project.dto.user.UserAuthoritiesDto;
import ru.otus.project.dto.user.UserInfoDto;
import ru.otus.project.dto.user.UserNoPasswordDto;
import ru.otus.project.dto.user.UserRequestDto;
import ru.otus.project.exception.EntityNotFoundException;
import ru.otus.project.exception.DataNotValidException;
import ru.otus.project.model.Address;
import ru.otus.project.service.UserService;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class UserRestController {
    private final UserService userService;

    @GetMapping("api/v1/user/")
    public List<UserInfoDto> readAllUsers() {
        return userService.findAll();
    }

    @GetMapping("api/v1/user/{id}")
    public UserNoPasswordDto readUserById(@PathVariable("id") long id) {
        return userService.findById(id).orElseThrow(
                () -> new EntityNotFoundException("User with id %d not found".formatted(id)));
    }

    @GetMapping("api/v1/user/home")
    public List<UserInfoDto> readAllUsersByHome(@Size(min = 3) @NotBlank @RequestParam String street,
                                                @NotNull @RequestParam Object streetNumber) {
        return userService.findByHome(street, streetNumber.toString());
    }

    @GetMapping("api/v1/user/district")
    public List<UserInfoDto> readAllUsersByDistrict(@Size(min = 3) @NotBlank @RequestParam String district) {
        return userService.findByDistrict(district);
    }

    @GetMapping("api/v1/user/authority/{authorityId}")
    public List<UserInfoDto> readAllUsersByAuthority(@PathVariable("authorityId") long authorityId) {
        return userService.findByAuthority(authorityId);
    }

    @PostMapping("/api/v1/user/")
    public void addUser(@Valid @RequestBody UserRequestDto user,
                             BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new DataNotValidException("Invalid data in request");
        }
        var address = new Address(user.getDistrict(), user.getStreet(), user.getStreetNumber(), user.getFlatNumber());
        userService.insert(user.getLogin(), user.getRawPassword(), address, user.getAuthorities());
    }

    @PatchMapping("api/v1/user/{id}/password")
    public void updateUserPassword(@PathVariable("id") long id,
                                   @Size(min = 3) @NotBlank @RequestParam String password) {
        userService.updatePassword(id, password);
    }

    @PatchMapping("api/v1/user/{id}/name")
    public void updateUserName(@PathVariable("id") long id,
                               @NotBlank @RequestParam String name) {
        userService.updateUserName(id, name);
    }

    @PatchMapping("api/v1/user/{id}/authority/")
    public void updateUserAuthorities(@PathVariable("id") long id,
                                      @Valid @RequestBody UserAuthoritiesDto user,
                                      BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new DataNotValidException("Invalid data in request");
        }
        userService.updateAuthorities(id, user.getAuthorities());
    }

    @DeleteMapping("/api/v1/user/{id}")
    public void deleteUser(@PathVariable("id") long id) {
        userService.delete(id);
    }
}
