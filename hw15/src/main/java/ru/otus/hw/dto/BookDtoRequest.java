package ru.otus.hw.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookDtoRequest {
    private long id;

    @NotBlank(message = "{title-field-should-not-be-blank}")
    @Size(max = 30, message = "{title-field-should-has-expected-size}")
    private String title;

    private long authorId;

    private long genreId;
}
