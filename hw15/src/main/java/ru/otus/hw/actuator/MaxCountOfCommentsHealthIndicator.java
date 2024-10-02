package ru.otus.hw.actuator;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.actuate.health.Status;
import org.springframework.stereotype.Component;
import ru.otus.hw.service.BookService;
import ru.otus.hw.service.CommentService;

import java.util.HashMap;

@RequiredArgsConstructor
@Component
public class MaxCountOfCommentsHealthIndicator implements HealthIndicator {

    private static final int MAX_COUNT_OF_COMMENTS = 5;

    private final BookService bookService;

    private final CommentService commentService;

    @Override
    public Health health() {
        var details = new HashMap<String, String>();
        var books = bookService.findAll();

        for (int i = 0; i < books.size(); i++) {
            long currentId = books.get(i).getId();
            int countOfComments = commentService.findAllCommentsByBookId(currentId).size();
            if (countOfComments > MAX_COUNT_OF_COMMENTS) {
                if (details.isEmpty()) {
                    details.put("cause", "Количество комментариев больше " + MAX_COUNT_OF_COMMENTS);
                }
                details.put("bookId_" + currentId, "Всего комментариев " + countOfComments);
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
