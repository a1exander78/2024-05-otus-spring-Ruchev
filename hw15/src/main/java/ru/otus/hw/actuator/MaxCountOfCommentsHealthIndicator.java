package ru.otus.hw.actuator;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.actuate.health.Status;
import org.springframework.stereotype.Component;
import ru.otus.hw.service.BookService;

import java.util.HashMap;

@RequiredArgsConstructor
@Component
public class MaxCountOfCommentsHealthIndicator implements HealthIndicator {

    private static final int MAX_COUNT_OF_COMMENTS = 1;

    private final BookService bookService;

    @Override
    public Health health() {
        var details = new HashMap<String, String>();
        int count = bookService.getCountOfBooksWithCommentsExcess(MAX_COUNT_OF_COMMENTS);

        if (count != 0) {
            details.put("message", "Превышено максимальное количество комментариев у " + endOfMessage(count));
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
            return count + " книги";
        }
        return count + " книг";
    }
}
