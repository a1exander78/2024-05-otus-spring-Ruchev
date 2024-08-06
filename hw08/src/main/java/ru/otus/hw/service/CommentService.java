package ru.otus.hw.service;

import ru.otus.hw.dto.CommentDto;

import java.util.List;
import java.util.Optional;

public interface CommentService {
    List<CommentDto> findAllCommentsByBookId(String bookId);

    Optional<CommentDto> findById(String id);

    CommentDto insert(String description, String bookId);

    CommentDto update(String id, String description);

    void deleteById(String id);
}
