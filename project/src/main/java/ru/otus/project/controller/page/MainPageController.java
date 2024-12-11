package ru.otus.project.controller.page;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.otus.project.exception.EntityNotFoundException;
import ru.otus.project.service.UserService;

import java.util.ArrayList;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Controller
public class MainPageController {
    private final UserService userService;

    @GetMapping({"/", "/main"})
    public String startPage(Model model) {
        setRoleAttribute(model);
        setIdAttribute(model);
        return "start";
    }

    void setRoleAttribute(Model model) {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        if (authentication.isAuthenticated()) {
            var user = (UserDetails) authentication.getPrincipal();

            var currentUserAuthorities = user.getAuthorities()
                    .stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toCollection(ArrayList::new));

            if (currentUserAuthorities.contains("ROLE_ADMIN")) {
                model.addAttribute("role", "ADMIN");
            } else {
                model.addAttribute("role", "USER");
            }
        }
    }

    void setIdAttribute(Model model) {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        if (authentication.isAuthenticated()) {
            var user = (UserDetails) authentication.getPrincipal();
            var login = user.getUsername();
            var userDto = userService.findByLogin(login).orElseThrow(
                    () -> new EntityNotFoundException("User with login %s not found".formatted(login)));

            model.addAttribute("userId", userDto.getId());
        }
    }
}