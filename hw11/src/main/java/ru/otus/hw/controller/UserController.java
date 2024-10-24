package ru.otus.hw.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.otus.hw.service.UserService;

@RequiredArgsConstructor
@Controller
public class UserController {
    private final UserService userService;

    @GetMapping("/user/")
    public String readAllUsers(Model model) {
        var users = userService.findAll();
        model.addAttribute("users", users);
        return "allUsers";
    }
}
