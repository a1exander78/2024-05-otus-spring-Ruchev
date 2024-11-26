package ru.otus.project.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.otus.project.model.Cart;
import ru.otus.project.model.Status;
import ru.otus.project.model.User;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе Jpa для работы с корзинами пользователей ")
@DataJpaTest
class CartRepositoryTest {
    @Autowired
    private CartRepository repository;

    @Autowired
    private TestEntityManager em;

    @DisplayName("должен загружать список всех корзин")
    @Test
    void shouldReturnCorrectUserList() {
        var actualCarts = repository.findAll();

        var expectedCart1 = em.find(Cart.class, 1L);
        var expectedCart2 = em.find(Cart.class, 2L);
        var expectedCart3 = em.find(Cart.class, 3L);
        var expectedCart4 = em.find(Cart.class, 4L);
        var expectedCart5 = em.find(Cart.class, 5L);

        assertThat(actualCarts).containsExactlyElementsOf(List.of(expectedCart1, expectedCart2, expectedCart3, expectedCart4, expectedCart5));

        actualCarts.forEach(System.out::println);
    }

    @DisplayName("должен загружать корзину по id")
    @Test
    void shouldReturnCorrectCartById() {
        var actualCart = repository.findById(1L);
        var expectedCart = em.find(Cart.class, 1L);
        assertThat(actualCart).isPresent()
                .get()
                .isEqualTo(expectedCart);

        var cart = actualCart.get();
        System.out.printf("Корзина пользователя %s имеет статус %s и содержит %d сумки",
                cart.getUser().getUserName(), cart.getCartStatus().getStatus(), cart.getBags().size());
    }

    @DisplayName("должен загружать список корзин по id пользователя")
    @Test
    void shouldReturnCorrectCartListByUserId() {
        var actualCarts = repository.findByUserId(3L);

        var expectedCart2 = em.find(Cart.class, 2L);
        var expectedCart5 = em.find(Cart.class, 5L);

        assertThat(actualCarts).containsExactlyElementsOf(List.of(expectedCart2, expectedCart5));

        System.out.println("Пользователь с id 3 имеет корзины со статусами: ");
        actualCarts.stream().map(Cart::getCartStatus).map(Status::getStatus).forEach(System.out::println);
    }

    @DisplayName("должен загружать список всех корзин по статусу")
    @Test
    void findByCartStatusId() {
        var actualCartsWithStatus1 = repository.findByCartStatusId(1L);
        var actualCartsWithStatus2 = repository.findByCartStatusId(2L);

        var expectedCart1 = em.find(Cart.class, 1L);
        var expectedCart2 = em.find(Cart.class, 2L);
        var expectedCart3 = em.find(Cart.class, 3L);
        var expectedCart4 = em.find(Cart.class, 4L);

        assertThat(actualCartsWithStatus1).containsExactlyElementsOf(List.of(expectedCart3, expectedCart4));
        assertThat(actualCartsWithStatus2).containsExactlyElementsOf(List.of(expectedCart1, expectedCart2));

        System.out.println("Пользователи со статусом корзин IN_PROCESSING: " + actualCartsWithStatus1.stream().map(Cart::getUser).map(User::getUserName).toList());
        System.out.println("Пользователи со статусом корзин NEW: " + actualCartsWithStatus2.stream().map(Cart::getUser).map(User::getUserName).toList());
    }

    @DisplayName("должен обновлять статус корзины")
    @Test
    void shouldUpdateStatus() {
        var actualCart = em.find(Cart.class, 1L);

        assertThat(actualCart.getCartStatus().getId()).isEqualTo(2L);

        var expectedCart = Cart.builder()
                .id(actualCart.getId())
                .cartStatus(new Status(3L, "PROCESSED"))
                .user(actualCart.getUser())
                .bags(actualCart.getBags())
                .build();

        assertThat(repository.updateStatus(expectedCart.getId(), new Status(3L, "PROCESSED"))).isOne();
    }
}