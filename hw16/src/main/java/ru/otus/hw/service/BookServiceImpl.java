package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.converter.dto.BookDtoConverter;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.exception.EntityNotFoundException;
import ru.otus.hw.model.Book;
import ru.otus.hw.repository.AuthorRepository;
import ru.otus.hw.repository.BookRepository;
import ru.otus.hw.repository.GenreRepository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {
    private final AuthorRepository authorRepository;

    private final GenreRepository genreRepository;

    private final BookRepository bookRepository;

    private final BookDtoConverter converter;

    @Override
    public List<BookDto> findAll() {
        return bookRepository.findAll().stream().map(converter::toDto).toList();
    }

    @Override
    public Optional<BookDto> findById(long id) {
        return bookRepository.findById(id).map(converter::toDto);
    }

    @Transactional
    @Override
    public BookDto insert(String title, long authorId, long genreId) {
        var newBook = save(0, title, authorId, genreId);
        return converter.toDto(newBook);
    }

    @Transactional
    @Override
    public BookDto update(long id, String title, long authorId, long genreId) {
        var newBook = save(id, title, authorId, genreId);
        return converter.toDto(newBook);
    }

    @Transactional
    @Override
    public void deleteById(long id) {
        bookRepository.deleteById(id);
    }

    private Book save(long id, String title, long authorId, long genreId) {
        var author = authorRepository.findById(authorId)
                .orElseThrow(() -> new EntityNotFoundException("Author with id %d not found".formatted(authorId)));
        var genre = genreRepository.findById(genreId)
                .orElseThrow(() -> new EntityNotFoundException("Genre with id %d not found".formatted(genreId)));
        var book = new Book(id, title, author, genre);
        return bookRepository.save(book);
    }
}
