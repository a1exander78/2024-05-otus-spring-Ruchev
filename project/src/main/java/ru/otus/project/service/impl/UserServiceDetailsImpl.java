package ru.otus.project.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.otus.project.repository.UserRepository;

@RequiredArgsConstructor
@Service
public class UserServiceDetailsImpl implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        var user = userRepository.findByLogin(login)
                .orElseThrow(() -> new UsernameNotFoundException("User with login %s not found".formatted(login)));
        return User.builder()
                .username(user.getLogin())
                .password(user.getPassword())
                .authorities(user.getAuthorities())
                .build();
    }
}
