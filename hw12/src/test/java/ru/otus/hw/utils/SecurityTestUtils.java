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
    public static final Authority ADMIN_AUTHORITY = new Authority(1L,"ROLE_ADMIN");
    public static final Authority USER_AUTHORITY = new Authority(1L,"ROLE_USER");

    protected static MockHttpServletRequestBuilder method2RequestBuilder(String method, String url) {
        Map<String, Function<String, MockHttpServletRequestBuilder>> methodMap = Map
                .of("get", MockMvcRequestBuilders::get,
                        "post", MockMvcRequestBuilders::post);
        return methodMap.get(method).apply(url);
    }

    protected static Stream<Arguments> generateData4MainController() {
        return Stream.of(
                Arguments.of("get", "/", null, null, 302, true),
                Arguments.of("get", "/", "user1_test", List.of(USER_AUTHORITY), 200, false),
                Arguments.of("get", "/", "admin_test", List.of(ADMIN_AUTHORITY, USER_AUTHORITY), 200, false)
        );
    }

    protected static Stream<Arguments> generateData4UserController() {
        return Stream.of(
                Arguments.of("get", "/user/", null, null, 302, true),
                Arguments.of("get", "/user/", "user1_test", List.of(USER_AUTHORITY), 200, false),
                Arguments.of("get", "/user/", "admin_test", List.of(ADMIN_AUTHORITY, USER_AUTHORITY), 200, false)
        );
    }

    protected static Stream<Arguments> generateData4BookController() {
        return Stream.of(
                Arguments.of("get", "/book/", null, null, 302, true),
                Arguments.of("get", "/book/", "user1_test", List.of(USER_AUTHORITY), 200, false),
                Arguments.of("get", "/book/", "admin_test", List.of(ADMIN_AUTHORITY, USER_AUTHORITY), 200, false),
                Arguments.of("get", "/book/1", null, null, 302, true),
                Arguments.of("get", "/book/1", "user1_test", List.of(USER_AUTHORITY), 403, false),
                Arguments.of("get", "/book/1", "admin_test", List.of(ADMIN_AUTHORITY, USER_AUTHORITY), 200, false),
                Arguments.of("post", "/book/1", null, null, 302, true),
                Arguments.of("post", "/book/1", "user1_test", List.of(USER_AUTHORITY), 403, false),
                Arguments.of("post", "/book/1", "admin_test", List.of(ADMIN_AUTHORITY, USER_AUTHORITY), 200, false),
                Arguments.of("get", "/book/new", null, null, 302, true),
                Arguments.of("get", "/book/new", "user1_test", List.of(USER_AUTHORITY), 200, false),
                Arguments.of("get", "/book/new", "admin_test", List.of(ADMIN_AUTHORITY, USER_AUTHORITY), 200, false),
                Arguments.of("post", "/book/new", null, null, 302, true),
                Arguments.of("post", "/book/new", "user1_test", List.of(USER_AUTHORITY), 200, false),
                Arguments.of("post", "/book/new", "admin_test", List.of(ADMIN_AUTHORITY, USER_AUTHORITY), 200, false),
                Arguments.of("get", "/book/1/del", null, null, 302, true),
                Arguments.of("get", "/book/1/del", "user1_test", List.of(USER_AUTHORITY), 200, false),
                Arguments.of("get", "/book/1/del", "admin_test", List.of(ADMIN_AUTHORITY, USER_AUTHORITY), 200, false),
                Arguments.of("post", "/book/1/del", null, null, 302, true),
                Arguments.of("post", "/book/1/del", "user1_test", List.of(USER_AUTHORITY), 302, true),
                Arguments.of("post", "/book/1/del", "admin_test", List.of(ADMIN_AUTHORITY, USER_AUTHORITY), 302, true)
        );
    }

    protected static Stream<Arguments> generateData4CommentController() {
        return Stream.of(
                Arguments.of("get", "/comment?bookId=1", null, null, 302, true),
                Arguments.of("get", "/comment?bookId=1", "user1_test", List.of(USER_AUTHORITY), 200, false),
                Arguments.of("get", "/comment?bookId=1", "admin_test", List.of(ADMIN_AUTHORITY, USER_AUTHORITY), 200, false),
                Arguments.of("get", "/comment/1", null, null, 302, true),
                Arguments.of("get", "/comment/1", "user1_test", List.of(USER_AUTHORITY), 200, false),
                Arguments.of("get", "/comment/1", "admin_test", List.of(ADMIN_AUTHORITY, USER_AUTHORITY), 200, false),
                Arguments.of("post", "/comment/1", null, null, 302, true),
                Arguments.of("post", "/comment/1", "user1_test", List.of(USER_AUTHORITY), 302, true),
                Arguments.of("post", "/comment/1", "admin_test", List.of(ADMIN_AUTHORITY, USER_AUTHORITY), 302, true),
                Arguments.of("get", "/comment/new?bookId=1", null, null, 302, true),
                Arguments.of("get", "/comment/new?bookId=1", "user1_test", List.of(USER_AUTHORITY), 200, false),
                Arguments.of("get", "/comment/new?bookId=1", "admin_test", List.of(ADMIN_AUTHORITY, USER_AUTHORITY), 200, false),
                Arguments.of("post", "/comment/new?bookId=1", null, null, 302, true),
                Arguments.of("post", "/comment/new?bookId=1", "user1_test", List.of(USER_AUTHORITY), 302, true),
                Arguments.of("post", "/comment/new?bookId=1", "admin_test", List.of(ADMIN_AUTHORITY, USER_AUTHORITY), 302, true),
                Arguments.of("get", "/comment/1/del", null, null, 302, true),
                Arguments.of("get", "/comment/1/del", "user1_test", List.of(USER_AUTHORITY), 200, false),
                Arguments.of("get", "/comment/1/del", "admin_test", List.of(ADMIN_AUTHORITY, USER_AUTHORITY), 200, false),
                Arguments.of("post", "/comment/1/del", null, null, 302, true),
                Arguments.of("post", "/comment/1/del", "user1_test", List.of(USER_AUTHORITY), 302, true),
                Arguments.of("post", "/comment/1/del", "admin_test", List.of(ADMIN_AUTHORITY, USER_AUTHORITY), 302, true)
        );
    }
}
