package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.converter.toDto.CommentToDtoConverter;
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

    private final CommentToDtoConverter commentToDtoConverter;

    @Override
    public List<CommentDto> findAllCommentsByBookId(String bookId) {
        var commentsList = commentRepository.findAllCommentsByBookId(bookId)
                .stream()
                .map(commentToDtoConverter::convert)
                .toList();
        if (commentsList.isEmpty()) {
            throw new EntityNotFoundException("Book with id %s not found or haven't got comments yet".formatted(bookId));
        }
        return commentsList;
    }

    @Override
    public Optional<CommentDto> findById(String id) {
        return commentRepository.findById(id).map(commentToDtoConverter::convert);
    }

    @Transactional
    @Override
    public CommentDto insert(String description, String bookId) {
        var newComment = save("", description, bookId);
        return commentToDtoConverter.convert(newComment);
    }

    @Transactional
    @Override
    public CommentDto update(String id, String description) {
        var comment = commentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Comment with id %s not found".formatted(id)));
        var bookId = comment.getBook().getId();
        var updatedComment = save(id, description, bookId);
        return commentToDtoConverter.convert(updatedComment);
    }

    @Transactional
    @Override
    public void deleteById(String id) {
        commentRepository.deleteById(id);
    }

    private Comment save(String id, String description, String bookId) {
        var book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Book with id %s not found".formatted(bookId)));
        Comment savedComment;
        if (id.equals("")) {
            savedComment = commentRepository.save(new Comment(description, book));
        } else {
            savedComment = commentRepository.save(new Comment(id, description, book));
        }
        return savedComment;
    }
}
