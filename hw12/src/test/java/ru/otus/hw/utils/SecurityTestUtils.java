package ru.otus.hw.utils;

import org.junit.jupiter.params.provider.Arguments;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.otus.hw.model.Authority;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;

public abstract class SecurityTestUtils {
    public static final Authority ADMIN = new Authority(1L,"ROLE_ADMIN");
    public static final Authority USER = new Authority(1L,"ROLE_USER");

    protected static MockHttpServletRequestBuilder method2RequestBuilder(String method, String url) {
        Map<String, Function<String, MockHttpServletRequestBuilder>> methodMap = Map
                .of("get", MockMvcRequestBuilders::get,
                        "post", MockMvcRequestBuilders::post);
        return methodMap.get(method).apply(url);
    }

    protected static Stream<Arguments> generateData4MainController() {
        return Stream.of(
                Arguments.of("get", "/", null, null, 302, true),
                Arguments.of("get", "/", "user", List.of(USER), 200, false),
                Arguments.of("get", "/", "admin", List.of(ADMIN, USER), 200, false)
        );
    }

    protected static Stream<Arguments> generateData4UserController() {
        return Stream.of(
                Arguments.of("get", "/user/", null, null, 302, true),
                Arguments.of("get", "/user/", "user", List.of(USER), 200, false),
                Arguments.of("get", "/user/", "admin", List.of(ADMIN, USER), 200, false)
        );
    }

    protected static Stream<Arguments> generateData4BookController() {
        return Stream.of(
                Arguments.of("get", "/book/", null, null, 302, true),
                Arguments.of("get", "/book/", "user", List.of(USER), 200, false),
                Arguments.of("get", "/book/", "admin", List.of(ADMIN, USER), 200, false),
                Arguments.of("get", "/book/1", null, null, 302, true),
                Arguments.of("get", "/book/1", "user", List.of(USER), 403, false),
                Arguments.of("get", "/book/1", "admin", List.of(ADMIN, USER), 200, false),
                Arguments.of("post", "/book/1", null, null, 302, true),
                Arguments.of("post", "/book/1", "user", List.of(USER), 403, false),
                Arguments.of("post", "/book/1", "admin", List.of(ADMIN, USER), 200, false),
                Arguments.of("get", "/book/new", null, null, 302, true),
                Arguments.of("get", "/book/new", "user", List.of(USER), 200, false),
                Arguments.of("get", "/book/new", "admin", List.of(ADMIN, USER), 200, false),
                Arguments.of("post", "/book/new", null, null, 302, true),
                Arguments.of("post", "/book/new", "user", List.of(USER), 200, false),
                Arguments.of("post", "/book/new", "admin", List.of(ADMIN, USER), 200, false),
                Arguments.of("get", "/book/1/del", null, null, 302, true),
                Arguments.of("get", "/book/1/del", "user", List.of(USER), 200, false),
                Arguments.of("get", "/book/1/del", "admin", List.of(ADMIN, USER), 200, false),
                Arguments.of("post", "/book/1/del", null, null, 302, true),
                Arguments.of("post", "/book/1/del", "user", List.of(USER), 302, true),
                Arguments.of("post", "/book/1/del", "admin", List.of(ADMIN, USER), 302, true)
        );
    }

    protected static Stream<Arguments> generateData4CommentController() {
        return Stream.of(
                Arguments.of("get", "/comment?bookId=1", null, null, 302, true),
                Arguments.of("get", "/comment?bookId=1", "user", List.of(USER), 200, false),
                Arguments.of("get", "/comment?bookId=1", "admin", List.of(ADMIN, USER), 200, false),
                Arguments.of("get", "/comment/1", null, null, 302, true),
                Arguments.of("get", "/comment/1", "user", List.of(USER), 200, false),
                Arguments.of("get", "/comment/1", "admin", List.of(ADMIN, USER), 200, false),
                Arguments.of("post", "/comment/1", null, null, 302, true),
                Arguments.of("post", "/comment/1", "user", List.of(USER), 302, true),
                Arguments.of("post", "/comment/1", "admin", List.of(ADMIN, USER), 302, true),
                Arguments.of("get", "/comment/new?bookId=1", null, null, 302, true),
                Arguments.of("get", "/comment/new?bookId=1", "user", List.of(USER), 200, false),
                Arguments.of("get", "/comment/new?bookId=1", "admin", List.of(ADMIN, USER), 200, false),
                Arguments.of("post", "/comment/new?bookId=1", null, null, 302, true),
                Arguments.of("post", "/comment/new?bookId=1", "user", List.of(USER), 302, true),
                Arguments.of("post", "/comment/new?bookId=1", "admin", List.of(ADMIN, USER), 302, true),
                Arguments.of("get", "/comment/1/del", null, null, 302, true),
                Arguments.of("get", "/comment/1/del", "user", List.of(USER), 200, false),
                Arguments.of("get", "/comment/1/del", "admin", List.of(ADMIN, USER), 200, false),
                Arguments.of("post", "/comment/1/del", null, null, 302, true),
                Arguments.of("post", "/comment/1/del", "user", List.of(USER), 302, true),
                Arguments.of("post", "/comment/1/del", "admin", List.of(ADMIN, USER), 302, true)
        );
    }
}
