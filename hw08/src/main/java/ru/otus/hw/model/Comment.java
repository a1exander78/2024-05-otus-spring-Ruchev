package ru.otus.hw.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document
public class Comment {
    @Id
    private String id;

    private String description;

    @DBRef
    private Book book;

    public Comment(String description, Book book) {
        this.description = description;
        this.book = book;
    }
}
