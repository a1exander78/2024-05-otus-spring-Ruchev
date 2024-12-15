package ru.otus.project.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.project.dto.cart.CartDto;
import ru.otus.project.dto.cart.CartStatusBagsDto;
import ru.otus.project.dto.cart.CartUserDto;
import ru.otus.project.exception.CustomQueryException;
import ru.otus.project.mapper.CartMapper;
import ru.otus.project.model.Cart;
import ru.otus.project.repository.CartRepository;
import ru.otus.project.service.CartService;
import ru.otus.project.service.util.EntityService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@RequiredArgsConstructor
@Service
public class CartServiceImpl implements CartService {
    private final CartRepository cartRepository;

    private final EntityService entityService;

    private final CartMapper mapper;

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @Override
    public List<CartStatusBagsDto> findAll() {
        return cartRepository.findAll().stream().map(mapper::toCartStatusBagsDto).toList();
    }

    @PostAuthorize("hasAuthority('ROLE_ADMIN') || returnObject.get().user.login == authentication.name")
    @Override
    public Optional<CartDto> findById(long id) {
        return cartRepository.findById(id).map(mapper::toDto);
    }

    @PreAuthorize("hasAuthority('ROLE_USER')")
    @Override
    public List<CartStatusBagsDto> findByUserId(long userId) {
        return cartRepository.findByUserId(userId).stream().map(mapper::toCartStatusBagsDto).toList();
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @Override
    public List<CartUserDto> findByStatusId(long statusId) {
        return cartRepository.findByCartStatusId(statusId).stream().map(mapper::toCartUserDto).toList();
    }

    @PreAuthorize("!hasAuthority('ROLE_ADMIN')")
    @Transactional
    @Override
    public CartDto insert(long userId, long bagId) {
        var newCart = save(0, 1, userId, bagId);
        return mapper.toDto(newCart);
    }

    @Transactional
    @Override
    public CartDto updateStatus(long id, long statusId) {
        var status = entityService.getStatusIfExists(statusId);
        if (cartRepository.updateStatus(id, status) != 1) {
            throw new CustomQueryException("Status was not updated");
        }
        return mapper.toDto(entityService.getCartIfExists(id));
    }

    @PreAuthorize("!hasAuthority('ROLE_ADMIN')")
    @Transactional
    @Override
    public CartDto addBag(long id, long bagId) {
        var cart = entityService.getCartIfExists(id);
        var bag = entityService.getBagIfExists(bagId);
        var cartBags = cart.getBags();
        cartBags.add(bag);
        return mapper.toDto(cartRepository.save(cart));
    }

    @PreAuthorize("!hasAuthority('ROLE_ADMIN')")
    @Transactional
    @Override
    public CartDto deleteBag(long id, long bagId) {
        var cart = entityService.getCartIfExists(id);
        var bag = entityService.getBagIfExists(bagId);
        var cartBags = cart.getBags();
        cartBags.remove(bag);
        return mapper.toDto(cartRepository.save(cart));
    }

    @PreAuthorize("!hasAuthority('ROLE_ADMIN')")
    @Override
    public void delete(long id) {
        cartRepository.deleteById(id);
    }

    private Cart save(long id, long statusId, long userId, long bagId) {
        return cartRepository.save(Cart.builder()
                .id(id)
                .cartStatus(entityService.getStatusIfExists(statusId))
                .user(entityService.getUserIfExists(userId))
                .bags(Stream.of(entityService.getBagIfExists(bagId)).toList())
                .build());
    }
}
