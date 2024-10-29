package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.otus.hw.repository.UserRepository;

@RequiredArgsConstructor
@Service
public class UserServiceDetailsImpl implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var user = userRepository.findByLogin(username)
                .orElseThrow(() -> new UsernameNotFoundException("User with name %s not found".formatted(username)));
        return User.builder()
                .username(user.getLogin())
                .password(user.getPassword())
                .authorities(user.getAuthorities())
                .build();
    }
}
