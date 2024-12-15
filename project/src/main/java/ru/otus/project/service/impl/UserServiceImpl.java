package ru.otus.project.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.project.dto.user.UserInfoDto;
import ru.otus.project.dto.user.UserNoPasswordDto;
import ru.otus.project.exception.CustomQueryException;
import ru.otus.project.exception.PasswordException;
import ru.otus.project.mapper.UserMapper;
import ru.otus.project.dto.user.UserDto;
import ru.otus.project.model.Address;
import ru.otus.project.model.Authority;
import ru.otus.project.model.User;
import ru.otus.project.repository.UserRepository;
import ru.otus.project.service.UserService;
import ru.otus.project.service.util.EntityService;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    private final EntityService entityService;

    private final UserMapper mapper;

    private final PasswordEncoder passwordEncoder;

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @Override
    public List<UserInfoDto> findAll() {
        return userRepository.findAll().stream().map(mapper::toInfoDto).toList();
    }

    @Override
    public Optional<UserNoPasswordDto> findById(long id) {
        return userRepository.findById(id).map(mapper::toNoPasswordDto);
    }

    @Override
    public Optional<UserDto> findByLogin(String login) {
        return userRepository.findByLogin(login).map(mapper::toDto);
    }

    //@PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @Override
    public List<UserInfoDto> findByHome(String street, String streetNumber) {
        return userRepository.findByAddressStreetAndAddressStreetNumber(street, streetNumber).stream()
                .map(mapper::toInfoDto)
                .toList();
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @Override
    public List<UserInfoDto> findByDistrict(String district) {
        return userRepository.findByAddressDistrict(district).stream().map(mapper::toInfoDto).toList();
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @Override
    public List<UserInfoDto> findByAuthority(long authority) {
        return userRepository.findByAuthoritiesIn(List.of(authority)).stream().map(mapper::toInfoDto).toList();
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @Transactional
    @Override
    public UserNoPasswordDto insert(String login, String password, Address address, List<Long> authorities) {
        var newUser = save(0, login, password, address, entityService.getAuthoritiesIfExists(authorities));
        return mapper.toNoPasswordDto(newUser);
    }

    @PreAuthorize("hasAuthority('ROLE_USER')")
    @Transactional
    @Override
    public UserDto updatePassword(long id, String password) {
        if (checkNewPassword(id, password)) {
            if (userRepository.updateUserPassword(id, passwordEncoder.encode(password)) != 1) {
                throw new CustomQueryException("Password was not updated");
            }
            return mapper.toDto(entityService.getUserIfExists(id));
        }
        throw new PasswordException("New password should be different from old");
    }

    @PreAuthorize("hasAuthority('ROLE_USER')")
    @Transactional
    @Override
    public UserNoPasswordDto updateUserName(long id, String userName) {
        if (userRepository.updateUserName(id, userName) != 1) {
            throw new CustomQueryException("Username was not updated");
        }
        return mapper.toNoPasswordDto(entityService.getUserIfExists(id));
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @Transactional
    @Override
    public UserNoPasswordDto updateAuthorities(long id, List<Long> authorityIds) {
        var user = entityService.getUserIfExists(id);
        var userAuthorities = user.getAuthorities();
        userAuthorities.clear();
        if (id == 1) {
            if (!authorityIds.contains(1L)) {
                authorityIds.add(1L);
            }
        }
        userAuthorities.addAll(entityService.getAuthoritiesIfExists(authorityIds));
        return mapper.toNoPasswordDto(userRepository.save(user));
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @Override
    public void delete(long id) {
        if (id != 1) {
            userRepository.deleteById(id);
        }
    }

    private User save(long id, String login, String password, Address address, List<Authority> authorities) {
        return userRepository.save(User.builder()
                .id(id)
                .login(login)
                .password(passwordEncoder.encode(password))
                .userName("")
                .address(address)
                .authorities(authorities)
                .build());
    }

    private boolean checkNewPassword(long id, String newPassword) {
        var currentPassword = entityService.getUserIfExists(id).getPassword();
        if (passwordEncoder.matches(newPassword, currentPassword)) {
            return false;
        }
        return true;
    }
}
