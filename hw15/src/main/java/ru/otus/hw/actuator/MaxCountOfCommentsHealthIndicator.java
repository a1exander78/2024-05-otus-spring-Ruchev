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

        if (!books.isEmpty()) {
            for (int i = 0; i < books.size(); i++) {
                long currentId = books.get(i);
                details.put("bookId_" + currentId, "Количество комментариев больше " + MAX_COUNT_OF_COMMENTS);
            }
        }

        if (!details.isEmpty()) {
            return Health.down().status(Status.DOWN).withDetails(details).build();
        }

        return Health.up()
                .withDetail("message", "Количество комментариев по каждой книге в пределах " + MAX_COUNT_OF_COMMENTS)
                .build();
    }
}
