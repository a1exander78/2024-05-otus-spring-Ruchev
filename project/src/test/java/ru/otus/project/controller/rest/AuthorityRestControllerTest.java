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
import ru.otus.project.exception.EntityUnprocessableException;
import ru.otus.project.service.AuthorityService;

import java.util.List;
import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("REST-контроллер Authority")
@WebMvcTest(controllers = AuthorityRestController.class,
        excludeAutoConfiguration = SecurityAutoConfiguration.class)
class AuthorityRestControllerTest extends TestData {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private AuthorityService authorityService;

    @DisplayName("должен возвращать корректный список Authorities")
    @Test
    void shouldReturnCorrectAuthoritiesList() throws Exception {
        var authorities = List.of(AUTHORITY_ADMIN, AUTHORITY_USER);
        given(authorityService.findAll()).willReturn(authorities);

        mvc.perform(get("/api/v1/authority/"))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(authorities)));
    }

    @DisplayName("должен возвращать Authority по айди")
    @Test
    void shouldReturnAuthorityById() throws Exception {
        given(authorityService.findById(ID_1)).willReturn(Optional.of(AUTHORITY_ADMIN));

        mvc.perform(get("/api/v1/authority/1"))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(AUTHORITY_ADMIN)));
    }

    @DisplayName("должен сохранять новую Authority")
    @Test
    void shouldSaveNewAuthority() throws Exception {
        var expectedResult = mapper.writeValueAsString(AUTHORITY_DRIVER);

        mvc.perform(post("/api/v1/authority/").contentType(APPLICATION_JSON)
                        .content(expectedResult))
                .andExpect(status().isOk());

        verify(authorityService, times(1)).insert("ROLE_DRIVER");
    }

    @DisplayName("не должен удалять Authority, если есть пользователи")
    @Test
    void shouldNotDeleteAuthority() throws Exception {
        given(authorityService.delete(ID_1)).willThrow(EntityUnprocessableException.class);

        mvc.perform(delete("/api/v1/authority/1"))
                .andExpect(status().is4xxClientError());

        verify(authorityService, times(1)).delete(ID_1);
    }
}