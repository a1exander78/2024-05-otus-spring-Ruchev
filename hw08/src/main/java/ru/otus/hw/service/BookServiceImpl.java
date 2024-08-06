package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.converter.toDto.BookToDtoConverter;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.exception.EntityNotFoundException;
import ru.otus.hw.exception.UnmodifyEntityException;
import ru.otus.hw.model.Book;
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

    @Transactional(readOnly = true)
    @Override
    public List<BookDto> findAll() {
        return bookRepository.findAll().stream().map(bookToDtoConverter::convert).toList();
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<BookDto> findById(String id) {
        return bookRepository.findById(id).map(bookToDtoConverter::convert);
    }

    @Transactional
    @Override
    public BookDto insert(String title, String authorId, String genreId) {
        return save("", title, authorId, genreId);
    }

    @Transactional
    @Override
    public BookDto update(String id, String title, String authorId, String genreId) {
        if (!commentRepository.findAllCommentsByBookId(id).isEmpty()) {
            throw new UnmodifyEntityException(
                    "Book with id %s has already bean commented. Remove comments first".formatted(id));
        }
        return save(id, title, authorId, genreId);
    }

    @Transactional
    @Override
    public void deleteById(String id) {
        commentRepository.deleteAllCommentsByBookId(id);
        bookRepository.deleteById(id);
    }

    private BookDto save(String id, String title, String authorId, String genreId) {
        var author = authorRepository.findById(authorId)
                .orElseThrow(() -> new EntityNotFoundException("Author with id %s not found".formatted(authorId)));
        var genre = genreRepository.findById(genreId)
                .orElseThrow(() -> new EntityNotFoundException("Genre with id %s not found".formatted(genreId)));

        if (id.equals("")) {
            var newBook = bookRepository.save(new Book(title, author, genre));
            return bookToDtoConverter.convert(newBook);
        }

        var updateBook = bookRepository.save(new Book(id, title, author, genre));
        return bookToDtoConverter.convert(updateBook);
    }
}
