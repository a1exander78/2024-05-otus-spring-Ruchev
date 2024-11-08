package ru.otus.hw.service;

import ru.otus.hw.dto.CommentDto;

import java.util.List;
import java.util.Optional;

public interface CommentService {
    List<CommentDto> findAllCommentsByBookId(long bookId);

    Optional<CommentDto> findById(long id);

    CommentDto insert(String description, long bookId, long userId);

    CommentDto update(long id, String description);

    void deleteById(long id);
}
