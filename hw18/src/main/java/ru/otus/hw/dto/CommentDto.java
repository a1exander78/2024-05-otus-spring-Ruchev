package ru.otus.hw.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentDto {
    @JsonSerialize(using = ToStringSerializer.class)
    private ObjectId id;

    private String description;

    @JsonSerialize(using = ToStringSerializer.class)
    private ObjectId bookId;
}