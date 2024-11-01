package ru.otus.hw.dto;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentDtoRequest {
    private long id;

    @Size(min = 5, max = 100, message = "{description-field-should-has-expected-size}")
    private String description;

    private long bookId;

    private long userId;
}
