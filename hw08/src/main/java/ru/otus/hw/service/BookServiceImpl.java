package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.converter.toDto.BookToDtoConverter;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.exception.EntityNotFoundException;
import ru.otus.hw.exception.UnmodifyEntityException;
import ru.otus.hw.model.Book;
import ru.otus.hw.model.Comment;
import ru.otus.hw.repository.AuthorRepository;
import ru.otus.hw.repository.BookRepository;
import ru.otus.hw.repository.CommentRepository;
import ru.otus.hw.repository.GenreRepository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {
    private final AuthorRepository authorRepository;

    private final GenreRepository genreRepository;

    private final BookRepository bookRepository;

    private final CommentRepository commentRepository;

    private final BookToDtoConverter bookToDtoConverter;

    @Override
    public List<BookDto> findAll() {
        return bookRepository.findAll().stream().map(bookToDtoConverter::convert).toList();
    }

    @Override
    public Optional<BookDto> findById(String id) {
        return bookRepository.findById(id).map(bookToDtoConverter::convert);
    }

    @Transactional
    @Override
    public BookDto insert(String title, String authorId, String genreId) {
        var newBook = save("", title, authorId, genreId);
        return bookToDtoConverter.convert(newBook);
    }

    @Transactional
    @Override
    public BookDto update(String id, String title, String authorId, String genreId) {
        if (commentRepository.existsByBookId(id)) {
            throw new UnmodifyEntityException(("Book with id %s has already bean commented." +
                    "Remove comments first or use force update").formatted(id));
        }
        var updatedBook = save(id, title, authorId, genreId);
        return bookToDtoConverter.convert(updatedBook);
    }

    @Transactional
    @Override
    public BookDto forceUpdate(String id, String title, String authorId, String genreId) {
        if (!commentRepository.existsByBookId(id)) {
            return update(id, title, authorId, genreId);
        }
        var updatedBook = save(id, title, authorId, genreId);
        var updateComments = commentRepository.findAllCommentsByBookId(id);
        for (var updateComment : updateComments) {
            commentRepository.save(
                    new Comment(updateComment.getId(), updateComment.getDescription(), updatedBook));
        }
        return bookToDtoConverter.convert(updatedBook);
    }

    @Transactional
    @Override
    public void deleteById(String id) {
        commentRepository.deleteAllCommentsByBookId(id);
        bookRepository.deleteById(id);
    }

    private Book save(String id, String title, String authorId, String genreId) {
        var author = authorRepository.findById(authorId)
                .orElseThrow(() -> new EntityNotFoundException("Author with id %s not found".formatted(authorId)));
        var genre = genreRepository.findById(genreId)
                .orElseThrow(() -> new EntityNotFoundException("Genre with id %s not found".formatted(genreId)));
        Book savedBook;
        if (id.equals("")) {
            savedBook = bookRepository.save(new Book(title, author, genre));
        } else {
            savedBook = bookRepository.save(new Book(id, title, author, genre));
        }
        return savedBook;
    }
}
