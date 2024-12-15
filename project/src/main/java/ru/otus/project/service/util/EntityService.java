package ru.otus.project.service.util;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.project.exception.EntityNotFoundException;
import ru.otus.project.model.Authority;
import ru.otus.project.model.Bag;
import ru.otus.project.model.Cart;
import ru.otus.project.model.Status;
import ru.otus.project.model.User;
import ru.otus.project.repository.AuthorityRepository;
import ru.otus.project.repository.BagRepository;
import ru.otus.project.repository.CartRepository;
import ru.otus.project.repository.StatusRepository;
import ru.otus.project.repository.UserRepository;

import java.util.List;

@RequiredArgsConstructor
@Service
public class EntityService {
    private final AuthorityRepository authorityRepository;

    private final BagRepository bagRepository;

    private final CartRepository cartRepository;

    private final StatusRepository statusRepository;

    private final UserRepository userRepository;

    public List<Authority> getAuthoritiesIfExists(List<Long> authorityIds) {
        return authorityIds.stream()
                .map(id -> authorityRepository.findById(id)
                        .orElseThrow(() ->
                                new EntityNotFoundException("Authority with id %d not found".formatted(id))))
                .toList();
    }

    public Bag getBagIfExists(long bagId) {
        return bagRepository.findById(bagId)
                .orElseThrow(() -> new EntityNotFoundException("Bag with id %d not found".formatted(bagId)));
    }

    public Cart getCartIfExists(long id) {
        return cartRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cart with id %d not found".formatted(id)));
    }

    public Status getStatusIfExists(long statusId) {
        return statusRepository.findById(statusId)
                .orElseThrow(() -> new EntityNotFoundException("Status with id %d not found".formatted(statusId)));
    }

    public User getUserIfExists(long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User with id %d not found".formatted(userId)));
    }
}
