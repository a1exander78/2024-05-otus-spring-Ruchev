package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Book;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.GenreRepository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {
    private final AuthorRepository authorRepository;

    private final GenreRepository genreRepository;

    private final BookRepository bookRepository;

    @Transactional(readOnly = true)
    @Override
    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Book> findById(String id) {
        return bookRepository.findById(id);
    }

    @Transactional
    @Override
    public Book insert(String title, String authorId, String genreId) {
        return save("", title, authorId, genreId);
    }

    @Transactional
    @Override
    public Book update(String id, String title, String authorId, String genreId) {
        return save(id, title, authorId, genreId);
    }

    @Transactional
    @Override
    public void deleteById(String id) {
        bookRepository.deleteById(id);
    }

    private Book save(String id, String title, String authorId, String genreId) {
        var author = authorRepository.findById(authorId)
                .orElseThrow(() -> new EntityNotFoundException("Author with id %s not found".formatted(authorId)));
        var genre = genreRepository.findById(genreId)
                .orElseThrow(() -> new EntityNotFoundException("Genre with id %s not found".formatted(genreId)));

        if (id.equals("")) {
            return bookRepository.save(new Book(title, author, genre));
        }

        return bookRepository.save(new Book(id, title, author, genre));
    }
}
