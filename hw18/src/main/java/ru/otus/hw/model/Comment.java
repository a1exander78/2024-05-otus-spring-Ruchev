package ru.otus.hw.model;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Document
public class Comment {
    @Id
    private ObjectId id;

    private String description;

    private Book book;

    public Comment(String description, Book book) {
        this.description = description;
        this.book = book;
    }
}
