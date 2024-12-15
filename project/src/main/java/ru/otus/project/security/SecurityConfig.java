package ru.otus.project.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.RegexRequestMatcher;
import ru.otus.project.props.SecurityProps;

@RequiredArgsConstructor
@EnableMethodSecurity
@EnableWebSecurity
@Configuration
public class SecurityConfig {
    private final SecurityProps properties;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        var accessPatternToUser = RegexRequestMatcher.regexMatcher("/user/[0-9]+");
        var accessPatternToUpdateUser = RegexRequestMatcher.regexMatcher("/user/[0-9]+/(name|password)");
        var accessPatternToCart = RegexRequestMatcher.regexMatcher("/user/[0-9]+/cart/.*");
        http
                .headers(headers -> headers
                        .frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement((session) -> session.sessionCreationPolicy(SessionCreationPolicy.ALWAYS))
                .authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers("/h2-console/**").permitAll()
                        .requestMatchers(accessPatternToUser).permitAll()
                        .requestMatchers(accessPatternToUpdateUser).hasAuthority("ROLE_USER")
                        .requestMatchers(accessPatternToCart).hasAuthority("ROLE_USER")
                        .requestMatchers("/user/**").hasAuthority("ROLE_ADMIN")
                        .requestMatchers("/cart/**").hasAuthority("ROLE_ADMIN")
                        .requestMatchers("/**").authenticated()
                        .anyRequest().denyAll()
                )
                .formLogin(Customizer.withDefaults())
                .rememberMe(rm -> rm.key(properties.getSalt()).tokenValiditySeconds(60));
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
