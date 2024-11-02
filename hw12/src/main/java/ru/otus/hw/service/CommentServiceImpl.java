package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.acls.domain.BasePermission;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.converter.dto.CommentDtoConverter;
import ru.otus.hw.dto.CommentDto;
import ru.otus.hw.exception.EntityNotFoundException;
import ru.otus.hw.model.Comment;
import ru.otus.hw.repository.BookRepository;
import ru.otus.hw.repository.CommentRepository;
import ru.otus.hw.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;

    private final BookRepository bookRepository;

    private final UserRepository userRepository;

    private final CommentDtoConverter converter;

    private final AclServiceWrapperService aclServiceWrapperService;

    @Override
    public List<CommentDto> findAllCommentsByBookId(long bookId) {
        var commentsList = commentRepository.findAllCommentsByBookId(bookId);
        if (commentsList.isEmpty()) {
            throw new EntityNotFoundException("Book with id %d haven't got comments yet".formatted(bookId));
        }
        return commentsList.stream().map(converter::toDto).toList();
    }

    @PreAuthorize("hasPermission(#id, 'ru.otus.hw.dto.CommentDto', 'READ')")
    @Override
    public Optional<CommentDto> findById(long id) {
        return commentRepository.findById(id).map(converter::toDto);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN') == false")
    @Transactional
    @Override
    public CommentDto insert(String description, long bookId, long userId) {
        var newComment = save(0, description, bookId, userId);
        var insertedComment = converter.toDto(newComment);
        aclServiceWrapperService.createPermission(insertedComment, BasePermission.READ);
        return insertedComment;
    }

    @Transactional
    @Override
    public CommentDto update(long id, String description) {
        var comment = commentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Comment with id %d not found".formatted(id)));
        var bookId = comment.getBook().getId();
        var userId = comment.getUser().getId();
        var updatedComment = save(id, description, bookId, userId);
        return converter.toDto(updatedComment);
    }

    @Transactional
    @Override
    public void deleteById(long id) {
        commentRepository.deleteById(id);
    }

    private Comment save(long id, String description, long bookId, long userId) {
        var book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Book with id %d not found".formatted(bookId)));
        var user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User with id %d not found".formatted(userId)));
        var comment = new Comment(id, description, book, user);
        return commentRepository.save(comment);
    }
}
