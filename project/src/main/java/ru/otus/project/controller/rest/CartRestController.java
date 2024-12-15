package ru.otus.project.controller.rest;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.project.props.AppProps;
import ru.otus.project.dto.cart.CartDto;
import ru.otus.project.dto.cart.CartRequestDto;
import ru.otus.project.dto.cart.CartStatusBagsDto;
import ru.otus.project.dto.cart.CartUserDto;
import ru.otus.project.dto.user.UserInfoDto;
import ru.otus.project.exception.EntityNotFoundException;
import ru.otus.project.exception.CartException;
import ru.otus.project.exception.DataNotValidException;
import ru.otus.project.service.CartService;
import ru.otus.project.service.UserService;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class CartRestController {
    private final AppProps properties;

    private final CartService cartService;

    private final UserService userService;

    @GetMapping("api/v1/cart/")
    public List<CartStatusBagsDto> readAllCarts() {
        return cartService.findAll();
    }

    @GetMapping("api/v1/cart/{cartId}")
    public CartDto readCartById(@PathVariable("cartId") long cartId) {
        return cartService.findById(cartId).orElseThrow(
                () -> new EntityNotFoundException("Cart with id %d not found".formatted(cartId)));
    }

    @GetMapping("api/v1/cart/user/{userId}")
    public List<CartStatusBagsDto> readAllCartsByUserId(@PathVariable("userId") long userId) {
        return cartService.findByUserId(userId);
    }

    @GetMapping("api/v1/cart/status/{statusId}")
    public List<CartUserDto> readAllCartsByStatusId(@PathVariable("statusId") long statusId) {
        return cartService.findByStatusId(statusId);
    }

    @PostMapping("/api/v1/cart/")
    public void addCart(@Valid @RequestBody CartRequestDto cart,
                             BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new DataNotValidException("Invalid data in request");
        }
        long userId = cart.getUserId();
        long bagId = cart.getBagId();
        if (!isAllUserCartsProcessed(userId)) {
            throw new CartException("You have active cart. At first you should to give away available bags");
        }
        cartService.insert(userId, bagId);
    }

    @PatchMapping("api/v1/cart/{cartId}/status")
    public void updateCartStatus(@PathVariable("cartId") long cartId, @RequestParam long statusId) {
        cartService.updateStatus(cartId, statusId);
    }

    @PatchMapping("api/v1/cart/{cartId}/bag")
    public void addBagToCart(@PathVariable("cartId") long cartId, @RequestParam long bagId) {
        var cart = cartService.findById(cartId).orElseThrow(
                () -> new EntityNotFoundException("Cart with id %d not found".formatted(cartId)));
        var user = cart.getUser();
        tryAddBagToCart(user, cart, bagId);
    }

    @DeleteMapping("api/v1/cart/{cartId}/bag")
    public void deleteBagFromCart(@PathVariable("cartId") long cartId, @RequestParam long bagId) {
        String status = getCartStatus(cartId);
        if (!status.equals("NEW")) {
            throw new CartException(("Can't delete bag from cart with status %s").formatted(status));
        }
        cartService.deleteBag(cartId, bagId);
    }

    @DeleteMapping("/api/v1/cart/{cartId}")
    public void deleteCart(@PathVariable("cartId") long cartId) {
        String status = getCartStatus(cartId);
        if (!status.equals("NEW")) {
            throw new CartException(("Can't delete cart with status %s").formatted(status));
        }
        cartService.delete(cartId);
    }

    private void tryAddBagToCart(UserInfoDto user, CartDto cart, long bagId) {
        String status = cart.getCartStatus().getStatus();
        long cartId = cart.getId();
        int countOfBags = cart.getBags().size();
        int limitPerCart = properties.getLimitForBagsInCart();
        int limitPerHome = properties.getLimitForBagsInHome();
        if (!status.equals("NEW")) {
            throw new CartException(("Can't add bag for cart with status %s, " +
                    "try create new cart").formatted(status));
        } else if (countOfBags == limitPerCart) {
            throw new CartException("Cart with id %d is full, try create new cart".formatted(cartId));
        } else if (countOfBags < limitPerCart) {
            cartService.addBag(cartId, bagId);
            var users = userService.findByHome(user.getAddress().getStreet(), user.getAddress().getStreetNumber());
            var carts = users.stream().map(u -> cartService.findByUserId(u.getId())).flatMap(List::stream).toList();
            var bags = carts.stream()
                    .filter(c -> c.getCartStatus().getStatus().equals("NEW"))
                    .map(CartStatusBagsDto::getBags)
                    .flatMap(List::stream)
                    .toList();
            if (bags.size() == limitPerHome) {
                carts.forEach(c -> cartService.updateStatus(c.getId(), 2L));
            }
        }
    }

    private String getCartStatus(long cartId) {
        var cart = cartService.findById(cartId).orElseThrow(
                () -> new EntityNotFoundException("Cart with id %d not found".formatted(cartId)));
        return cart.getCartStatus().getStatus();
    }

    private boolean isAllUserCartsProcessed(long userId) {
        return cartService.findByUserId(userId).stream()
                .map(c -> c.getCartStatus().getId())
                .allMatch((statusId -> statusId == 3L));
    }
}
