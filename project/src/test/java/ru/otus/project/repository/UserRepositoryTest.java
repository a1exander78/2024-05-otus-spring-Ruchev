package ru.otus.project.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.otus.project.model.User;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@DisplayName("Репозиторий на основе Jpa для работы с пользователями ")
@DataJpaTest
class UserRepositoryTest {
    @Autowired
    private UserRepository repository;

    @MockBean
    private PasswordEncoder encoder;

    @Autowired
    private TestEntityManager em;

    @DisplayName("должен загружать список всех пользователей")
    @Test
    void shouldReturnCorrectUserList() {
        var actualUsers = repository.findAll();

        var expectedUser1 = em.find(User.class, 1L);
        var expectedUser2 = em.find(User.class, 2L);
        var expectedUser3 = em.find(User.class, 3L);
        var expectedUser4 = em.find(User.class, 4L);
        var expectedUser5 = em.find(User.class, 5L);

        assertThat(actualUsers).containsExactlyElementsOf(List.of(expectedUser1, expectedUser2, expectedUser3, expectedUser4, expectedUser5));

        actualUsers.forEach(System.out::println);
    }

    @DisplayName("должен загружать пользователя по id")
    @Test
    void shouldReturnCorrectUserById() {
        var actualUser = repository.findById(1L);
        var expectedUser = em.find(User.class, 1L);
        assertThat(actualUser).isPresent()
                .get()
                .isEqualTo(expectedUser);

        System.out.println(actualUser.get().getUserName());
    }

    @DisplayName("должен загружать пользователя по login")
    @Test
    void shouldReturnCorrectUserByLogin() {
        var actualUser = repository.findByLogin("admin");
        var expectedUser = em.find(User.class, 1L);
        assertThat(actualUser).isPresent()
                .get()
                .isEqualTo(expectedUser);

        System.out.println(actualUser.get().getUserName());
    }

    @DisplayName("должен загружать список пользователей по улице и дому")
    @Test
    void shouldReturnCorrectUserListByStreetNumber() {
        var actualUsersFromLenina = repository.findByAddressStreetAndAddressStreetNumber("lenina", "1");
        var actualUsersFromPushkina = repository.findByAddressStreetAndAddressStreetNumber("pushkina", "2");

        var expectedUser2 = em.find(User.class, 2L);
        var expectedUser3 = em.find(User.class, 3L);
        var expectedUser4 = em.find(User.class, 4L);
        var expectedUser5 = em.find(User.class, 5L);

        assertThat(actualUsersFromLenina).containsExactlyElementsOf(List.of(expectedUser2, expectedUser3));
        assertThat(actualUsersFromPushkina).containsExactlyElementsOf(List.of(expectedUser4, expectedUser5));

        System.out.println("Пользователи с улицы lenina: " + actualUsersFromLenina.stream().map(User::getUserName).toList());
        System.out.println("Пользователи с улицы pushkina: " + actualUsersFromPushkina.stream().map(User::getUserName).toList());
    }

    @DisplayName("должен загружать список пользователей по району")
    @Test
    void shouldReturnCorrectUserListByDistrict() {
        var actualUsers = repository.findByAddressDistrict("central");

        var expectedUser2 = em.find(User.class, 2L);
        var expectedUser3 = em.find(User.class, 3L);
        var expectedUser4 = em.find(User.class, 4L);
        var expectedUser5 = em.find(User.class, 5L);

        assertThat(actualUsers).containsExactlyElementsOf(List.of(expectedUser2, expectedUser3, expectedUser4, expectedUser5));

        actualUsers.forEach(System.out::println);
    }

    @DisplayName("должен обновлять пароль")
    @Test
    void shouldUpdateUserPassword() {
        String newPassword = "SASHA";
        given(encoder.encode(newPassword)).willReturn("$2a$12$aXHLL6Ni1QFVGOtTAXYzZ.3FhTy7kMb9d2N0UI5WdvU/m1JXiipSy");

        var actualUser = em.find(User.class, 2L);

        System.out.println("Было: " + actualUser.getPassword());

        assertThat(actualUser.getPassword()).isEqualTo("$2a$12$TTmL9DtXP5EvS/6f9Omm4eRUShdv4bYYw6fk3qhJGj0kWsf.FKXEq");

        var expectedUser = User.builder()
                .id(2L)
                .login(actualUser.getLogin())
                .password(encoder.encode(newPassword))
                .userName(actualUser.getUserName())
                .address(actualUser.getAddress())
                .build();

        assertThat(repository.findById(expectedUser.getId()))
                .isPresent()
                .get()
                .isNotEqualTo(expectedUser);

        assertThat(repository.updateUserPassword(expectedUser.getId(), expectedUser.getPassword())).isOne();

        System.out.println(" -> Стало: " + em.persistFlushFind(actualUser).getPassword());
    }

    @DisplayName("должен обновлять имя пользователя")
    @Test
    void shouldUpdateUserName() {
        var actualUser = em.find(User.class, 2L);

        assertThat(actualUser.getUserName()).isEqualTo("sasha");

        var expectedUser = User.builder()
                .id(2L)
                .login(actualUser.getLogin())
                .password(actualUser.getPassword())
                .userName("SASHA")
                .address(actualUser.getAddress())
                .build();

        assertThat(repository.findById(expectedUser.getId()))
                .isPresent()
                .get()
                .isNotEqualTo(expectedUser);

        assertThat(repository.updateUserName(expectedUser.getId(), expectedUser.getUserName())).isOne();
    }
}