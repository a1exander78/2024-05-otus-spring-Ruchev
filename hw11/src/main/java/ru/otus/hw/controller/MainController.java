package ru.otus.hw.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.otus.hw.model.Role;

@RequiredArgsConstructor
@Controller
public class MainController {
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
            var authorities = AuthorityUtils.createAuthorityList(Role.ADMIN.getAuthority());

            if (user.getAuthorities().containsAll(authorities)) {
                model.addAttribute("role", Role.ADMIN.getAuthority());
                System.out.println("ADMIN");
            } else {
                model.addAttribute("role", Role.USER.getAuthority());
                System.out.println("USER");
            }
        }
    }
}