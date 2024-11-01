package ru.otus.hw.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.model.Authority;
import ru.otus.hw.security.SecurityConfig;
import ru.otus.hw.service.UserService;
import ru.otus.hw.utils.SecurityTestUtils;

import java.util.List;

import static java.util.Objects.nonNull;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Контроллер пользователей")
@Import(SecurityConfig.class)
@WebMvcTest(UserController.class)
class UserControllerSecurityTest extends SecurityTestUtils {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserService userService;

    @DisplayName("с учетом наличия/отсутствия авторизации должен вернуть")
    @MethodSource("generateData4UserController")
    @ParameterizedTest(name = "при {0} запросе на \"{1}\" для пользователя {2} статус {4}")
    void shouldReturnCorrectStatus(String method, String url, String userName, List<Authority> authorities, int status, boolean checkLoginRedirection) throws Exception {
        var request = method2RequestBuilder(method, url);

        if (nonNull(userName)) {
            request = request.with(user(userName).authorities(authorities));
        }
        var resultActions = mvc.perform(request).andExpect(status().is(status));
        if (checkLoginRedirection) {
            resultActions.andExpect(redirectedUrlPattern("**/login"));
        }
    }
}