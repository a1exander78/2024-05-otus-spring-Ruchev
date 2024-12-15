package ru.otus.project.props;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;

@Getter
@ConfigurationProperties(prefix = "security-props")
public class SecurityProps {
    private final String salt;

    @ConstructorBinding
    public SecurityProps(String salt) {
        this.salt = salt;
    }
}
