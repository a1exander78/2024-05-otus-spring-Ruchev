package ru.otus.project.controller.rest;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.project.TestData;
import ru.otus.project.service.CartService;
import ru.otus.project.service.UserService;

import java.util.List;
import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("REST-контроллер корзин")
@WebMvcTest(controllers = CartRestController.class,
        excludeAutoConfiguration = SecurityAutoConfiguration.class)
class CartRestControllerTest extends TestData {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private CartService cartService;

    @MockBean
    private UserService userService;

    @DisplayName("должен возвращать корректный список корзин")
    @Test
    void shouldReturnCorrectCartsList() throws Exception {
        var carts = List.of(CART_STATUS_BAGS_1, CART_STATUS_BAGS_2, CART_STATUS_BAGS_3, CART_STATUS_BAGS_4, CART_STATUS_BAGS_5);
        given(cartService.findAll()).willReturn(carts);

        mvc.perform(get("/api/v1/cart/"))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(carts)));
    }

    @DisplayName("должен возвращать корзину по айди")
    @Test
    void shouldReturnCartById() throws Exception {
        given(cartService.findById(ID_1)).willReturn(Optional.of(CART_1));

        mvc.perform(get("/api/v1/cart/1"))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(CART_1)));
    }

    @DisplayName("должен возвращать корректный список корзин пользователя")
    @Test
    void shouldReturnCorrectCartsListByUser() throws Exception {
        given(cartService.findByUserId(ID_3)).willReturn(List.of(CART_STATUS_BAGS_2, CART_STATUS_BAGS_5));

        mvc.perform(get("/api/v1/cart/user/3"))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(List.of(CART_STATUS_BAGS_2, CART_STATUS_BAGS_5))));
    }

    @DisplayName("должен возвращать корректный список корзин по статусу")
    @Test
    void shouldReturnCorrectCartsListByStatus() throws Exception {
        given(cartService.findByStatusId(ID_1)).willReturn(List.of(CART_USER_3, CART_USER_4));

        mvc.perform(get("/api/v1/cart/status/1"))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(List.of(CART_USER_3, CART_USER_4))));
    }

    @DisplayName("не должен сохранять новую корзину, если есть незавершенные")
    @Test
    void shouldNotSaveNewCart() throws Exception {
        given(cartService.findByUserId(ID_2)).willReturn(List.of(CART_STATUS_BAGS_1));
        var requestBody = mapper.writeValueAsString(NEW_CART);

        mvc.perform(post("/api/v1/cart/").contentType(APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest());

        verify(cartService, times(0)).insert(NEW_CART.getUserId(), NEW_CART.getBagId());
    }

    @DisplayName("должен сохранять новую корзину, если нет незавершенных")
    @Test
    void shouldSaveNewCart() throws Exception {
        given(cartService.findByUserId(ID_2)).willReturn(List.of());
        var requestBody = mapper.writeValueAsString(NEW_CART);

        mvc.perform(post("/api/v1/cart/").contentType(APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk());

        verify(cartService, times(1)).insert(NEW_CART.getUserId(), NEW_CART.getBagId());
    }

    @DisplayName("должен обновлять статус корзины")
    @Test
    void shouldUpdateCartStatus() throws Exception {
        mvc.perform(patch("/api/v1/cart/3/status?statusId=2"))
                .andExpect(status().isOk());

        verify(cartService, times(1)).updateStatus(ID_3, ID_2);
    }

    @DisplayName("должен обновлять статус корзины, по достижении лимита сумок в доме")
    @Test
    void shouldUpdateCartStatusIfCountOfBagsInHomeIsLimit() throws Exception {
        given(cartService.findById(ID_4)).willReturn(Optional.of(CART_4));
        given(userService.findByHome("pushkina", "2")).willReturn(List.of(USER_MISHA_INFO, USER_MASHA_INFO));
        given(cartService.findByUserId((ID_4))).willReturn(List.of(CART_STATUS_BAGS_3));
        given(cartService.findByUserId((ID_5))).willReturn(List.of(CART_STATUS_BAGS_4));

        mvc.perform(patch("/api/v1/cart/4/bag?bagId=3"))
                .andExpect(status().isOk());

        verify(cartService, times(1)).addBag(ID_4, ID_3);
        verify(cartService, times(1)).updateStatus(ID_3, ID_2);
        verify(cartService, times(1)).updateStatus(ID_4, ID_2);
    }

    @DisplayName("не должен добавлять сумку в корзину, если ее статус отличен от NEW")
    @Test
    void shouldNotAddBagToCart() throws Exception {
        given(cartService.findById(ID_1)).willReturn(Optional.of(CART_1));
        mvc.perform(patch("/api/v1/cart/1/bag?bagId=3"))
                .andExpect(status().isBadRequest());

        verify(cartService, times(0)).addBag(ID_1, ID_3);
    }

    @DisplayName("не должен добавлять сумку в корзину, если в ней уже есть 4 сумки")
    @Test
    void shouldNotAddBagToCartIfCountOfBagsIs4() throws Exception {
        given(cartService.findById(ID_3)).willReturn(Optional.of(CART_3));
        mvc.perform(patch("/api/v1/cart/3/bag?bagId=3"))
                .andExpect(status().isBadRequest());

        verify(cartService, times(0)).addBag(ID_3, ID_3);
    }

    @DisplayName("должен добавлять сумку в корзину")
    @Test
    void shouldAddBagToCart() throws Exception {
        given(cartService.findById(ID_4)).willReturn(Optional.of(CART_4));
        mvc.perform(patch("/api/v1/cart/4/bag?bagId=3"))
                .andExpect(status().isOk());

        verify(cartService, times(1)).addBag(ID_4, ID_3);
    }

    @DisplayName("не должен удалять сумку из корзины, если ее статус не NEW")
    @Test
    void shouldNotDeleteBagFromCartIfStatusIsNotNew() throws Exception {
        given(cartService.findById(ID_1)).willReturn(Optional.of(CART_1));
        mvc.perform(delete("/api/v1/cart/1/bag?bagId=2"))
                .andExpect(status().isBadRequest());

        verify(cartService, times(0)).deleteBag(ID_1, ID_2);
    }

    @DisplayName("должен удалять сумку из корзины")
    @Test
    void shouldDeleteBagFromCart() throws Exception {
        given(cartService.findById(ID_3)).willReturn(Optional.of(CART_3));
        mvc.perform(delete("/api/v1/cart/3/bag?bagId=2"))
                .andExpect(status().isOk());

        verify(cartService, times(1)).deleteBag(ID_3, ID_2);
    }

    @DisplayName("не должен удалять корзину, если ее статус не NEW")
    @Test
    void shouldNotDeleteCartIfStatusIsNotNew() throws Exception {
        given(cartService.findById(ID_1)).willReturn(Optional.of(CART_1));
        mvc.perform(delete("/api/v1/cart/1"))
                .andExpect(status().isBadRequest());

        verify(cartService, times(0)).delete(ID_1);
    }

    @DisplayName("должен удалять корзину")
    @Test
    void shouldDeleteCart() throws Exception {
        given(cartService.findById(ID_4)).willReturn(Optional.of(CART_4));
        mvc.perform(delete("/api/v1/cart/4"))
                .andExpect(status().isOk());

        verify(cartService, times(1)).delete(ID_4);
    }
}