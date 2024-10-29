package ru.otus.hw.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.otus.hw.security.SecurityProps;

import java.util.ArrayList;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Controller
public class MainController {
    private final SecurityProps props;

    @GetMapping({"/", "/main"})
    public String startPage(Model model) {
        setRoleAttribute(model);
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

            var permittedAuthorities = props.getAuthorities();

            if (currentUserAuthorities.containsAll(permittedAuthorities)) {
                model.addAttribute("role", "ADMIN");
            } else {
                model.addAttribute("role", "USER");
            }
        }
    }
}