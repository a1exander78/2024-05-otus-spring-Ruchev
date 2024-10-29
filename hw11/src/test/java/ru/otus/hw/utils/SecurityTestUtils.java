package ru.otus.hw.utils;

import org.junit.jupiter.params.provider.Arguments;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;

public abstract class SecurityTestUtils {
    private static String[] ADMIN_ROLES = new String[] {"ADMIN", "USER"};
    private static String[] USER_ROLES = new String[] {"USER"};

    protected static MockHttpServletRequestBuilder method2RequestBuilder(String method, String url) {
        Map<String, Function<String, MockHttpServletRequestBuilder>> methodMap = Map
                .of("get", MockMvcRequestBuilders::get,
                        "post", MockMvcRequestBuilders::post);
        return methodMap.get(method).apply(url);
    }

    protected static Stream<Arguments> generateData4MainController() {
        return Stream.of(
                Arguments.of("get", "/", null, null, 302, true),
                Arguments.of("get", "/", "user", USER_ROLES, 200, false),
                Arguments.of("get", "/", "admin", ADMIN_ROLES, 200, false)
        );
    }

    protected static Stream<Arguments> generateData4BookController() {
        return Stream.of(
                Arguments.of("get", "/book/", null, null, 302, true),
                Arguments.of("get", "/book/", "user", USER_ROLES, 200, false),
                Arguments.of("get", "/book/", "admin", ADMIN_ROLES, 200, false),
                Arguments.of("get", "/book/1", null, null, 302, true),
                Arguments.of("get", "/book/1", "user", USER_ROLES, 403, false),
                Arguments.of("get", "/book/1", "admin", ADMIN_ROLES, 200, false),
                Arguments.of("post", "/book/1", null, null, 302, true),
                Arguments.of("post", "/book/1", "user", USER_ROLES, 403, false),
                Arguments.of("post", "/book/1", "admin", ADMIN_ROLES, 200, false),
                Arguments.of("get", "/book/new", null, null, 302, true),
                Arguments.of("get", "/book/new", "user", USER_ROLES, 200, false),
                Arguments.of("get", "/book/new", "admin", ADMIN_ROLES, 200, false),
                Arguments.of("post", "/book/new", null, null, 302, true),
                Arguments.of("post", "/book/new", "user", USER_ROLES, 200, false),
                Arguments.of("post", "/book/new", "admin", ADMIN_ROLES, 200, false),
                Arguments.of("get", "/book/1/del", null, null, 302, true),
                Arguments.of("get", "/book/1/del", "user", USER_ROLES, 200, false),
                Arguments.of("get", "/book/1/del", "admin", ADMIN_ROLES, 200, false),
                Arguments.of("post", "/book/1/del", null, null, 302, true),
                Arguments.of("post", "/book/1/del", "user", USER_ROLES, 302, true),
                Arguments.of("post", "/book/1/del", "admin", ADMIN_ROLES, 302, true)
        );
    }

    protected static Stream<Arguments> generateData4CommentController() {
        return Stream.of(
                Arguments.of("get", "/comment?bookId=1", null, null, 302, true),
                Arguments.of("get", "/comment?bookId=1", "user", USER_ROLES, 200, false),
                Arguments.of("get", "/comment?bookId=1", "admin", ADMIN_ROLES, 200, false),
                Arguments.of("get", "/comment/1", null, null, 302, true),
                Arguments.of("get", "/comment/1", "user", USER_ROLES, 200, false),
                Arguments.of("get", "/comment/1", "admin", ADMIN_ROLES, 200, false),
                Arguments.of("post", "/comment/1", null, null, 302, true),
                Arguments.of("post", "/comment/1", "user", USER_ROLES, 302, true),
                Arguments.of("post", "/comment/1", "admin", ADMIN_ROLES, 302, true),
                Arguments.of("get", "/comment/new?bookId=1", null, null, 302, true),
                Arguments.of("get", "/comment/new?bookId=1", "user", USER_ROLES, 200, false),
                Arguments.of("get", "/comment/new?bookId=1", "admin", ADMIN_ROLES, 200, false),
                Arguments.of("post", "/comment/new?bookId=1", null, null, 302, true),
                Arguments.of("post", "/comment/new?bookId=1", "user", USER_ROLES, 302, true),
                Arguments.of("post", "/comment/new?bookId=1", "admin", ADMIN_ROLES, 302, true),
                Arguments.of("get", "/comment/1/del", null, null, 302, true),
                Arguments.of("get", "/comment/1/del", "user", USER_ROLES, 200, false),
                Arguments.of("get", "/comment/1/del", "admin", ADMIN_ROLES, 200, false),
                Arguments.of("post", "/comment/1/del", null, null, 302, true),
                Arguments.of("post", "/comment/1/del", "user", USER_ROLES, 302, true),
                Arguments.of("post", "/comment/1/del", "admin", ADMIN_ROLES, 302, true)
        );
    }
}
