package ru.otus.hw.actuator;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.actuate.health.Status;
import org.springframework.stereotype.Component;
import ru.otus.hw.repository.CommentRepository;

import java.util.HashMap;

@RequiredArgsConstructor
@Component
public class MaxCountOfCommentsHealthIndicator implements HealthIndicator {

    private static final int MAX_COUNT_OF_COMMENTS = 1;

    private final CommentRepository commentRepository;

    @Override
    public Health health() {
        var details = new HashMap<String, String>();
        var books = commentRepository.findBooksWithCommentsExcess(MAX_COUNT_OF_COMMENTS);
        int count = books.size();

        if (!books.isEmpty()) {
            String message = "Превышено максимальное количество комментариев у " + books.size() + endOfMessage(count);
            details.put("message", message);
        }

        if (!details.isEmpty()) {
            return Health.down().status(Status.DOWN).withDetails(details).build();
        }

        return Health.up()
                .withDetail("message", "Количество комментариев по каждой книге в пределах " + MAX_COUNT_OF_COMMENTS)
                .build();
    }

    String endOfMessage(int count) {
        if ((count - 1) % 10 == 0) {
            return " книги";
        }
        return " книг";
    }
}
