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
import ru.otus.project.model.Address;
import ru.otus.project.service.UserService;

import java.util.List;
import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("REST-контроллер пользователей")
@WebMvcTest(controllers = UserRestController.class,
        excludeAutoConfiguration = SecurityAutoConfiguration.class)
class UserRestControllerTest extends TestData {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private UserService userService;

    @DisplayName("должен возвращать корректный список пользователей")
    @Test
    void shouldReturnCorrectUsersList() throws Exception {
        var users = List.of(ADMIN_INFO, USER_SASHA_INFO, USER_DASHA_INFO, USER_MISHA_INFO, USER_MASHA_INFO);
        given(userService.findAll()).willReturn(users);

        mvc.perform(get("/api/v1/user/"))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(users)));
    }

    @DisplayName("должен возвращать пользователя по айди")
    @Test
    void shouldReturnUserById() throws Exception {
        given(userService.findById(ID_1)).willReturn(Optional.of(ADMIN_NO_PASSWORD));

        mvc.perform(get("/api/v1/user/1"))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(ADMIN_NO_PASSWORD)));
    }

    @DisplayName("должен возвращать корректный список пользователей по дому")
    @Test
    void shouldReturnCorrectUsersListByHome() throws Exception {
        given(userService.findByHome("lenina", "1")).willReturn(List.of(USER_SASHA_INFO, USER_DASHA_INFO));

        mvc.perform(get("/api/v1/user/home").param("street", "lenina").param("streetNumber", "1"))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(List.of(USER_SASHA_INFO, USER_DASHA_INFO))));
    }

    @DisplayName("должен возвращать корректный список пользователей по району")
    @Test
    void shouldReturnCorrectUsersListByDistrict() throws Exception {
        given(userService.findByDistrict("central")).willReturn(List.of(USER_SASHA_INFO, USER_DASHA_INFO, USER_MISHA_INFO, USER_MASHA_INFO));

        mvc.perform(get("/api/v1/user/district").param("district", "central"))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(List.of(USER_SASHA_INFO, USER_DASHA_INFO, USER_MISHA_INFO, USER_MASHA_INFO))));
    }

    @DisplayName("должен возвращать список пользователей по Authority")
    @Test
    void shouldReturnUsersListByAuthorityId() throws Exception {
        given(userService.findByAuthority(ID_1)).willReturn(List.of(ADMIN_INFO));

        mvc.perform(get("/api/v1/user/authority/1"))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(List.of(ADMIN_INFO))));
    }

    @DisplayName("должен сохранять нового пользователя")
    @Test
    void shouldSaveNewUser() throws Exception {
        var requestBody = mapper.writeValueAsString(USER_GRISHA_ADDRESS);

        mvc.perform(post("/api/v1/user/").contentType(APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk());

        verify(userService, times(1)).insert(
                USER_GRISHA_ADDRESS.getLogin(),
                USER_GRISHA_ADDRESS.getRawPassword(),
                new Address(USER_GRISHA_ADDRESS.getDistrict(),
                        USER_GRISHA_ADDRESS.getStreet(),
                        USER_GRISHA_ADDRESS.getStreetNumber(),
                        USER_GRISHA_ADDRESS.getFlatNumber()),
                USER_GRISHA_ADDRESS.getAuthorities());
    }

    @DisplayName("должен обновлять пароль")
    @Test
    void shouldUpdatePassword() throws Exception {
        mvc.perform(patch("/api/v1/user/2/password")
                        .param("password", "newPassword"))
                .andExpect(status().isOk());

        verify(userService, times(1)).updatePassword(ID_2, "newPassword");
    }

    @DisplayName("должен обновлять username")
    @Test
    void shouldUpdateUserName() throws Exception {
        mvc.perform(patch("/api/v1/user/2/name")
                        .param("name", "newName"))
                .andExpect(status().isOk());

        verify(userService, times(1)).updateUserName(ID_2, "newName");
    }

    @DisplayName("должен обновлять список Authorities")
    @Test
    void shouldUpdateAuthorities() throws Exception {
        var requestBody = mapper.writeValueAsString(USER_SASHA_NEW_AUTHORITIES);

        mvc.perform(patch("/api/v1/user/2/authority/")
                        .contentType(APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk());

        verify(userService, times(1)).updateAuthorities(ID_2, USER_SASHA_NEW_AUTHORITIES.getAuthorities());
    }

    @DisplayName("должен удалять пользователя")
    @Test
    void shouldDeleteUser() throws Exception {
        mvc.perform(delete("/api/v1/user/2"))
                .andExpect(status().isOk());

        verify(userService, times(1)).delete(ID_2);
    }
}