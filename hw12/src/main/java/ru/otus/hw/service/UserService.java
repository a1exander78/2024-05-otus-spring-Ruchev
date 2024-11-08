package ru.otus.hw.service;

import ru.otus.hw.model.User;

import java.util.List;

public interface UserService {
    List<User> findAll();

    User findByLogin(String login);
}
