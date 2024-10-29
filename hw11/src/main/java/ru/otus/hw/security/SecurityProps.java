package ru.otus.hw.security;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;

import java.util.List;

@Getter
@ConfigurationProperties(prefix = "security")
public class SecurityProps {
    private final String key;

    private final List<String> authorities;

    @ConstructorBinding
    public SecurityProps(String key, List<String> authorities) {
        this.key = key;
        this.authorities = authorities;
    }
}
