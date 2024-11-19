package ru.otus.hw.service;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.converter.dto.CommentDtoConverter;
import ru.otus.hw.dto.CommentDto;
import ru.otus.hw.exception.EntityNotFoundException;
import ru.otus.hw.model.Comment;
import ru.otus.hw.repository.BookRepository;
import ru.otus.hw.repository.CommentRepository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;

    private final BookRepository bookRepository;

    private final CommentDtoConverter converter;

    @CircuitBreaker(name = "serviceCircuitBreaker")
    @Override
    public List<CommentDto> findAllCommentsByBookId(long bookId) {
        var commentsList = commentRepository.findAllCommentsByBookId(bookId);
        if (commentsList.isEmpty()) {
            throw new EntityNotFoundException("Book with id %d haven't got comments yet".formatted(bookId));
        }
        return commentsList.stream().map(converter::toDto).toList();
    }

    @CircuitBreaker(name = "serviceCircuitBreaker")
    @Override
    public Optional<CommentDto> findById(long id) {
        return commentRepository.findById(id).map(converter::toDto);
    }

    @CircuitBreaker(name = "serviceCircuitBreaker")
    @Transactional
    @Override
    public CommentDto insert(String description, long bookId) {
        var newComment = save(0, description, bookId);
        return converter.toDto(newComment);
    }

    @CircuitBreaker(name = "serviceCircuitBreaker")
    @Transactional
    @Override
    public CommentDto update(long id, String description) {
        var comment = commentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Comment with id %d not found".formatted(id)));
        var bookId = comment.getBook().getId();
        var updatedComment = save(id, description, bookId);
        return converter.toDto(updatedComment);
    }

    @CircuitBreaker(name = "serviceCircuitBreaker")
    @Transactional
    @Override
    public void deleteById(long id) {
        commentRepository.deleteById(id);
    }

    private Comment save(long id, String description, long bookId) {
        var book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Book with id %d not found".formatted(bookId)));
        var comment = new Comment(id, description, book);
        return commentRepository.save(comment);
    }
}
