package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.model.User;
import ru.otus.hw.repository.UserRepository;

import java.util.List;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public User findByLogin(String login) {
        return findByLogin(login);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }
}
