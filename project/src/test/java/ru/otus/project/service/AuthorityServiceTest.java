package ru.otus.project.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.project.mapper.AuthorityMapperImpl;
import ru.otus.project.service.impl.AuthorityServiceImpl;
import ru.otus.project.TestData;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional(propagation = Propagation.NEVER)
@DisplayName("Сервис для работы с книгами")
@DataJpaTest
@Import({AuthorityServiceImpl.class, AuthorityMapperImpl.class})
class AuthorityServiceTest extends TestData {

    @Autowired
    private AuthorityService authorityService;

    @DisplayName("должен загружать список всех Authorities")
    @Test
    void shouldReturnCorrectAuthoritiesList() {
        assertThat(authorityService.findAll())
                .containsExactlyElementsOf(List.of(AUTHORITY_ADMIN, AUTHORITY_USER));
    }

    @DisplayName("должен загружать Authority по id")
    @Test
    void shouldReturnCorrectAuthorityById() {
        assertThat(authorityService.findById(ID_2)).contains(AUTHORITY_USER);
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @DisplayName("должен сохранять новую Authority")
    @Test
    void shouldSaveNewAuthority() {
        assertThat(authorityService.findAll().size()).isEqualTo(2);
        authorityService.insert("ROLE_DRIVER");

        var newAuthority = authorityService.findAll().stream().skip(2).findFirst();

        assertThat(newAuthority).isPresent();
        assertThat(newAuthority.get().getAuthority().equals("ROLE_DRIVER")).isTrue();
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @DisplayName("не должен удалять Authority, у которой имеются пользователи")
    @Test
    void shouldNotDeleteAuthorityByIdThatAlreadyHasUsers() {
        assertThat(authorityService.delete(ID_2)).isEqualTo(0);
    }
}