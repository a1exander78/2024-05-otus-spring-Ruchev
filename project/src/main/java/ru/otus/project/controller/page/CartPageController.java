package ru.otus.project.controller.page;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RequiredArgsConstructor
@Controller
public class CartPageController {
    private final MainPageController mainPageController;

    @GetMapping("/cart/")
    public String readAllCarts() {
        return "allCarts";
    }

    @GetMapping("/user/{userId}/cart/{cartId}")
    public String readCartById(@PathVariable("userId") long userId,
                               @PathVariable("cartId") long cartId, Model model) {
        mainPageController.setRoleAttribute(model);
        if (model.getAttribute("role").equals("USER")) {
            mainPageController.setIdAttribute(model);
        }
        return "singleCart";
    }

    @GetMapping("/user/{userId}/cart/")
    public String readAllCartsByUserId(@PathVariable("userId") long userId, Model model) {
        mainPageController.setRoleAttribute(model);
        if (model.getAttribute("role").equals("USER")) {
            mainPageController.setIdAttribute(model);
        }
        return "allCartsByUser";
    }

    @GetMapping("/cart/status/{statusId}")
    public String readAllCartsByStatusId(@PathVariable("statusId") long statusId) {
        return "allCartsByStatus";
    }

    @GetMapping("/user/{userId}/cart/add")
    public String addCart(@PathVariable("userId") long userId, Model model) {
        mainPageController.setIdAttribute(model);
        return "addCart";
    }

    @GetMapping("/cart/{cartId}/status/update")
    public String updateCartStatus(@PathVariable("cartId") long cartId) {
        return "updateCartStatus";
    }

    @GetMapping("/user/{userId}/cart/{cartId}/bag/add")
    public String addBagToCart(@PathVariable("userId") long userId,
                               @PathVariable("cartId") long cartId, Model model) {
        mainPageController.setIdAttribute(model);
        return "addBagToCart";
    }

    @GetMapping("/user/{userId}/cart/{cartId}/bag/delete")
    public String deleteBagFromCart(@PathVariable("userId") long userId,
                                    @PathVariable("cartId") long cartId, Model model) {
        mainPageController.setIdAttribute(model);
        return "deleteBagFromCart";
    }

    @GetMapping("/user/{userId}/cart/{cartId}/delete")
    public String deleteCart(@PathVariable("userId") long userId,
                             @PathVariable("cartId") long cartId, Model model) {
        mainPageController.setIdAttribute(model);
        return "deleteCart";
    }
}