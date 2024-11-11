package ru.otus.hw.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookDtoRequest {
    @JsonSerialize(using = ToStringSerializer.class)
    private ObjectId id;

    @NotBlank(message = "{title-field-should-not-be-blank}")
    @Size(max = 30, message = "{title-field-should-has-expected-size}")
    private String title;

    @JsonSerialize(using = ToStringSerializer.class)
    private ObjectId authorId;

    @JsonSerialize(using = ToStringSerializer.class)
    private ObjectId genreId;
}
