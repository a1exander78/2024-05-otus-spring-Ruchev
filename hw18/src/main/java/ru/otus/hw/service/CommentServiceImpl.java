package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
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

    @Override
    public List<CommentDto> findAllCommentsByBookId(ObjectId bookId) {
        return commentRepository.findAllCommentsByBookId(bookId).stream().map(converter::toDto).toList();
    }

    @Override
    public Optional<CommentDto> findById(ObjectId id) {
        return commentRepository.findById(id).map(converter::toDto);
    }

    @Transactional
    @Override
    public CommentDto insert(String description, ObjectId bookId) {
        var newComment = save(new ObjectId(), description, bookId);
        return converter.toDto(newComment);
    }

    @Transactional
    @Override
    public CommentDto update(ObjectId id, String description) {
        var comment = commentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Comment with id %s not found".formatted(id)));
        var bookId = comment.getBook().getId();
        var updatedComment = save(id, description, bookId);
        return converter.toDto(updatedComment);
    }

    @Transactional
    @Override
    public void deleteById(ObjectId id) {
        commentRepository.deleteById(id);
    }

    private Comment save(ObjectId id, String description, ObjectId bookId) {
        var book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Book with id %s not found".formatted(bookId)));
        var comment = new Comment(id, description, book);
        return commentRepository.save(comment);
    }
}
