package ru.otus.hw.service;

import org.springframework.security.core.userdetails.UserDetails;
import ru.otus.hw.model.User;

import java.util.List;

public interface UserService {
    List<User> findAll();

    UserDetails findByLogin(String login);
}
