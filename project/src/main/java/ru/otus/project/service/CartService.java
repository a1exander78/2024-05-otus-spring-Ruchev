package ru.otus.project.service;

import ru.otus.project.dto.cart.CartDto;
import ru.otus.project.dto.cart.CartStatusBagsDto;
import ru.otus.project.dto.cart.CartUserDto;

import java.util.List;
import java.util.Optional;

public interface CartService {
    List<CartStatusBagsDto> findAll();

    Optional<CartDto> findById(long id);

    List<CartStatusBagsDto> findByUserId(long userId);

    List<CartUserDto> findByStatusId(long statusId);

    CartDto insert(long userId, long bagId);

    CartDto updateStatus(long id, long statusId);

    CartDto addBag(long id, long bagId);

    CartDto deleteBag(long id, long bagId);

    void delete(long id);
}
