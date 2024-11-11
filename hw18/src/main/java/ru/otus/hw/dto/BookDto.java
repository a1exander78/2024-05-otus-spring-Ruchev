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
public class BookDto {
    @JsonSerialize(using = ToStringSerializer.class)
    private ObjectId id;

    private String title;

    private AuthorDto author;

    private GenreDto genre;
}
