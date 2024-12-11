package ru.otus.project.controller.page;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@RequiredArgsConstructor
@Controller
public class UserPageController {
    private final MainPageController mainPageController;

    @GetMapping("/user/")
    public String readAllUsers() {
        return "allUsers";
    }

    @GetMapping("/user/home")
    public String readAllUsersByHome(@RequestParam("street") String street,
                                     @RequestParam("streetNumber") String streetNumber) {
        return "allUsersByHome";
    }

    @GetMapping("/user/district")
    public String readAllUsersByDistrict(@RequestParam("district") String district) {
        return "allUsersByDistrict";
    }

    /*
    Дописать view
     */
    @GetMapping("/user/authority/{authorityId}")
    public String readAllUsersByAuthority(@PathVariable("authorityId") long authorityId) {
        return "allUsersByAuthority";
    }

    @GetMapping("/user/{userId}")
    public String readUserById(@PathVariable("userId") long userId, Model model) {
        mainPageController.setRoleAttribute(model);
        if (model.getAttribute("role").equals("USER")) {
            mainPageController.setIdAttribute(model);
        }
        return "singleUser";
    }

    @GetMapping("/user/add")
    public String addUser() {
        return "addUser";
    }

    @GetMapping("/user/{userId}/authority")
    public String editUserAuthority(@PathVariable("userId") long userId) {
        return "editUserAuthority";
    }

    @GetMapping("/user/{userId}/name")
    public String editUserName(@PathVariable("userId") long userId , Model model) {
        mainPageController.setIdAttribute(model);
        return "editUserName";
    }

    @GetMapping("/user/{userId}/password")
    public String editUserPassword(@PathVariable("userId") long userId , Model model) {
        mainPageController.setIdAttribute(model);
        return "editUserPassword";
    }

    @GetMapping("/user/{userId}/delete")
    public String deleteUser(@PathVariable("userId") long userId) {
        return "deleteUser";
    }
}
