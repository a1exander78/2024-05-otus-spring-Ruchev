package ru.otus.project.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.project.mapper.CartMapperImpl;
import ru.otus.project.service.impl.CartServiceImpl;
import ru.otus.project.service.util.EntityService;
import ru.otus.project.TestData;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional(propagation = Propagation.NEVER)
@DisplayName("Сервис для работы с корзинами")
@DataJpaTest
@Import({CartServiceImpl.class, CartMapperImpl.class, EntityService.class})
class CartServiceTest extends TestData {
    @Autowired
    private CartService cartService;

    @DisplayName("должен загружать список всех корзин")
    @Test
    void shouldReturnCorrectCartsList() {
        assertThat(cartService.findAll())
                .containsExactlyElementsOf(List.of(CART_STATUS_BAGS_1, CART_STATUS_BAGS_2, CART_STATUS_BAGS_3, CART_STATUS_BAGS_4, CART_STATUS_BAGS_5));
    }

    @DisplayName("должен загружать корзину по id")
    @Test
    void shouldReturnCorrectCartById() {
        assertThat(cartService.findById(ID_2)).contains(CART_2);
    }

    @DisplayName("должен загружать корзину по userId")
    @Test
    void shouldReturnCorrectCartsListByUserId() {
        assertThat(cartService.findByUserId(ID_3))
                .containsExactlyElementsOf(List.of(CART_STATUS_BAGS_2, CART_STATUS_BAGS_5));
    }

    @DisplayName("должен загружать корзину по statusId")
    @Test
    void shouldReturnCorrectCartsListByStatusId() {
        assertThat(cartService.findByStatusId(ID_1))
                .containsExactlyElementsOf(List.of(CART_USER_3, CART_USER_4));
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @DisplayName("должен сохранять новую корзину")
    @Test
    void shouldSaveNewCart() {
        assertThat(cartService.findAll().size()).isEqualTo(5);
        cartService.insert(ID_2, ID_4);

        assertThat(cartService.findById(6L))
                .hasValueSatisfying(c -> {
                        assertThat(c.getBags()).containsExactlyElementsOf(List.of(BAG_MIXED));
                        assertThat(c.getCartStatus()).isEqualTo(STATUS_NEW);
                });
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @DisplayName("должен обновлять статус корзины")
    @Test
    void shouldUpdateStatus() {
        assertThat(cartService.findById(ID_1)).contains(CART_1);
        cartService.updateStatus(ID_1, ID_3);

        assertThat(cartService.findById(ID_1))
                .hasValueSatisfying(c -> {
                    assertThat(c.getCartStatus().equals(STATUS_PROCESSED)).isTrue();
                });
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @DisplayName("должен добавлять сумку в корзину")
    @Test
    void shouldAddBagIntoCart() {
        assertThat(cartService.findById(ID_1)).contains(CART_1);
        cartService.addBag(ID_1, ID_4);

        assertThat(cartService.findById(ID_1))
                .hasValueSatisfying(c -> {
                    assertThat(c.equals(CART_1)).isFalse();
                    assertThat(c.getBags()).containsExactlyElementsOf(List.of(BAG_GLASS, BAG_PAPER, BAG_MIXED));
                });
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @DisplayName("должен удалять сумку из корзины")
    @Test
    void shouldDeleteBagFromCart() {
        assertThat(cartService.findById(ID_3)).contains(CART_3);
        cartService.deleteBag(ID_3, ID_3);

        assertThat(cartService.findById(ID_3))
                .hasValueSatisfying(c -> {
                    assertThat(c.equals(CART_3)).isFalse();
                    assertThat(c.getBags()).containsExactlyElementsOf(List.of(BAG_GLASS, BAG_PAPER, BAG_PLASTIC));
                });
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @DisplayName("должен удалять корзину по id")
    @Test
    void shouldDeleteCartById() {
        assertThat(cartService.findById(ID_1)).contains(CART_1);
        cartService.delete(ID_1);
        assertThat(cartService.findById(ID_1)).isEmpty();
    }
}