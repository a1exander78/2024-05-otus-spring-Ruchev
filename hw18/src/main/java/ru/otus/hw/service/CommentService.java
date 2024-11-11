package ru.otus.hw.service;

import org.bson.types.ObjectId;
import ru.otus.hw.dto.CommentDto;

import java.util.List;
import java.util.Optional;

public interface CommentService {
    List<CommentDto> findAllCommentsByBookId(ObjectId bookId);

    Optional<CommentDto> findById(ObjectId id);

    CommentDto insert(String description, ObjectId bookId);

    CommentDto update(ObjectId id, String description);

    void deleteById(ObjectId id);
}
