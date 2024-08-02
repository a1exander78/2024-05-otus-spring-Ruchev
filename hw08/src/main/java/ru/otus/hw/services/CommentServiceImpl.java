package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Comment;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.CommentRepository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;

    private final BookRepository bookRepository;

    @Override
    public List<Comment> findAllCommentsByBookId(String bookId) {
        var commentsList = commentRepository.findAllCommentsByBookId(bookId);
        if (commentsList.isEmpty()) {
            throw new EntityNotFoundException("Book with id %d not found or haven't got comments yet".formatted(bookId));
        }
        return commentsList;
    }

    @Override
    public Optional<Comment> findById(String id) {
        return commentRepository.findById(id);
    }

    @Transactional
    @Override
    public Comment insert(String description, String bookId) {
        return save("", description, bookId);
    }

    @Transactional
    @Override
    public Comment update(String id, String description) {
        var comment = commentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Comment with id %s not found".formatted(id)));
        var bookId = comment.getBook().getId();
        return save(id, description, bookId);
    }

    @Transactional
    @Override
    public void deleteById(String id) {
        commentRepository.deleteById(id);
    }

    private Comment save(String id, String description, String bookId) {
        var book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Book with id %s not found".formatted(bookId)));
        var comment = new Comment(id, description, book);

        if (id.equals("")) {
            return commentRepository.save(new Comment(description, book));
        }
        return commentRepository.save(new Comment(id, description, book));
    }
}
