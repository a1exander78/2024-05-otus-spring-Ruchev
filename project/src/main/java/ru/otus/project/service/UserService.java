package ru.otus.project.service;

import ru.otus.project.dto.user.UserDto;
import ru.otus.project.dto.user.UserInfoDto;
import ru.otus.project.dto.user.UserNoPasswordDto;
import ru.otus.project.model.Address;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<UserInfoDto> findAll();

    Optional<UserNoPasswordDto> findById(long id);

    Optional<UserDto> findByLogin(String login);

    List<UserInfoDto> findByHome(String street, String streetNumber);

    List<UserInfoDto> findByDistrict(String district);

    List<UserInfoDto> findByAuthority(long authority);

    UserNoPasswordDto insert(String login, String password, Address address, List<Long> authorityIds);

    UserDto updatePassword(long id, String password);

    UserNoPasswordDto updateUserName(long id, String userName);

    UserNoPasswordDto updateAuthorities(long id, List<Long> authorityIds);

    void delete(long id);
}
