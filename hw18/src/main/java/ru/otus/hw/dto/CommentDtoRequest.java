package ru.otus.hw.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentDtoRequest {
    @JsonSerialize(using = ToStringSerializer.class)
    private ObjectId id;

    @Size(min = 5, max = 100, message = "{description-field-should-has-expected-size}")
    private String description;

    private ObjectId bookId;
}
