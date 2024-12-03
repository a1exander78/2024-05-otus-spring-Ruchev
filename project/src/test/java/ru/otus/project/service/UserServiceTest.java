package ru.otus.project.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.project.exception.PasswordException;
import ru.otus.project.mapper.UserMapperImpl;
import ru.otus.project.service.impl.UserServiceImpl;
import ru.otus.project.service.util.EntityService;
import ru.otus.project.TestData;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Transactional(propagation = Propagation.NEVER)
@DisplayName("Сервис для работы с пользователями")
@DataJpaTest
@Import({UserServiceImpl.class, UserMapperImpl.class, EntityService.class, BCryptPasswordEncoder.class})
class UserServiceTest extends TestData {
    @Autowired
    private UserService userService;

    @DisplayName("должен загружать список всех пользователей")
    @Test
    void shouldReturnCorrectCartsList() {
        assertThat(userService.findAll())
                .containsExactlyElementsOf(List.of(ADMIN_INFO, USER_SASHA_INFO, USER_DASHA_INFO, USER_MISHA_INFO, USER_MASHA_INFO));
    }

    @DisplayName("должен загружать пользователя по id")
    @Test
    void shouldReturnCorrectCartById() {
        assertThat(userService.findById(ID_2)).contains(USER_SASHA_NO_PASSWORD);
    }

    @DisplayName("должен загружать пользователя по login")
    @Test
    void shouldReturnCorrectCartByLogin() {
        assertThat(userService.findByLogin("1_1")).contains(USER_SASHA);
    }

    @DisplayName("должен загружать список пользователей по дому")
    @Test
    void shouldReturnCorrectUsersListByHome() {
        assertThat(userService.findByHome("lenina", "1"))
                .containsExactlyElementsOf(List.of(USER_SASHA_INFO, USER_DASHA_INFO));
    }

    @DisplayName("должен загружать список пользователей по району")
    @Test
    void shouldReturnCorrectUsersListByDistrict() {
        assertThat(userService.findByDistrict("central"))
                .containsExactlyElementsOf(List.of(USER_SASHA_INFO, USER_DASHA_INFO, USER_MISHA_INFO, USER_MASHA_INFO));
    }

    @DisplayName("должен загружать список пользователей по Authority")
    @Test
    void shouldReturnCorrectUsersListByAuthority() {
        assertThat(userService.findByAuthority(ID_1))
                .containsExactlyElementsOf(List.of(ADMIN_INFO));
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @DisplayName("должен сохранять нового пользователя")
    @Test
    void shouldSaveNewUser() {
        String newPassword = "13_13";

        assertThat(userService.findAll().size()).isEqualTo(5);
        userService.insert("13_13", newPassword, AYVAZOVSKOGO_13_13, List.of(ID_2));

        assertThat(userService.findById(6L))
                .hasValueSatisfying(u -> {
                    assertThat(u.getLogin().equals("13_13")).isTrue();
                    assertThat(u.getAuthorities()).containsExactlyElementsOf(List.of(AUTHORITY_USER));
                });
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @DisplayName("должен обновлять пароль пользователя")
    @Test
    void shouldUpdatePassword() {
        String newPassword = "SASHA";
        assertThat(userService.findById(ID_2)).contains(USER_SASHA_NO_PASSWORD);
        userService.updatePassword(ID_2, newPassword);

        assertThat(userService.findByLogin(USER_SASHA_NO_PASSWORD.getLogin()))
                .hasValueSatisfying(u -> {
                    System.out.println(u.getPassword());
                    assertThat(u.getPassword().equals(USER_SASHA.getPassword())).isFalse();
                });
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @DisplayName("не должен обновлять пароль пользователя, если он такой же, как и был")
    @Test
    void shouldNotUpdatePassword() {
        String newPassword = "1_1";

        assertThatThrownBy(() -> userService.updatePassword(ID_2, newPassword)).isInstanceOf(PasswordException.class);
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @DisplayName("должен обновлять username")
    @Test
    void shouldUpdateUserName() {
        assertThat(userService.findById(ID_2)).contains(USER_SASHA_NO_PASSWORD);
        userService.updateUserName(ID_2, "SASHA");

        assertThat(userService.findById(ID_2))
                .hasValueSatisfying(u ->
                        assertThat(u.getUserName().equals("SASHA")).isTrue()
                );
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @DisplayName("должен обновлять список Authorities пользователя")
    @Test
    void shouldUpdateUserAuthorities() {
        assertThat(userService.findById(ID_2)).contains(USER_SASHA_NO_PASSWORD);
        userService.updateAuthorities(ID_2, List.of(ID_1, ID_2));

        assertThat(userService.findById(ID_2))
                .hasValueSatisfying(u ->
                        assertThat(u.getAuthorities()).containsExactlyElementsOf(List.of(AUTHORITY_ADMIN, AUTHORITY_USER))
                );
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @DisplayName("должен удалять пользователя по id")
    @Test
    void shouldDeleteUserById() {
        assertThat(userService.findById(ID_2)).contains(USER_SASHA_NO_PASSWORD);
        userService.delete(ID_2);

        assertThat(userService.findById(ID_2)).isEmpty();
    }
}