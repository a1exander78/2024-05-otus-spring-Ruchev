package ru.otus.hw.controller.rest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ExceptionHandler;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.converter.dto.BookDtoConverter;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.BookDtoRequest;
import ru.otus.hw.exception.EntityNotFoundException;
import ru.otus.hw.model.Book;
import ru.otus.hw.repository.reactive.AuthorRepository;
import ru.otus.hw.repository.reactive.BookRepository;
import ru.otus.hw.repository.reactive.GenreRepository;

@RequiredArgsConstructor
@RestController
public class BookRestController {
    private final BookRepository bookRepository;

    private final AuthorRepository authorRepository;

    private final GenreRepository genreRepository;

    private final BookDtoConverter converter;

    @GetMapping("/api/v1/book/")
    public Flux<BookDto> readAllBooks() {
        return bookRepository.findAll().map(converter::toDto);
    }

    @GetMapping("/api/v1/book/{id}")
    public Mono<ResponseEntity<BookDto>> readBook(@PathVariable("id") ObjectId id) {
        return bookRepository.findById(id)
                .map(converter::toDto)
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.error(new EntityNotFoundException("Book with id %s not found".formatted(id))));
    }

    @PutMapping("/api/v1/book/{id}")
    public Mono<BookDto> updateBook(@Valid @RequestBody BookDtoRequest book,
                             BindingResult bindingResult) {
        if (!bindingResult.hasErrors()) {
            return bookRepository.save(Book.builder()
                            .id(book.getId())
                            .title(book.getTitle())
                            .author(authorRepository.findById(book.getAuthorId()).block())
                            .genre(genreRepository.findById(book.getGenreId()).block())
                            .build())
                    .map(converter::toDto);
        }
        return Mono.empty();
    }

    @PostMapping("/api/v1/book")
    public Mono<BookDto> addBook(@Valid @RequestBody BookDtoRequest book,
                           BindingResult bindingResult) {
        if (!bindingResult.hasErrors()) {
            return bookRepository.save(Book.builder()
                    .id(new ObjectId())
                    .title(book.getTitle())
                    .author(authorRepository.findById(book.getAuthorId()).block())
                    .genre(genreRepository.findById(book.getGenreId()).block())
                    .build())
                    .map(converter::toDto);
        }
        return Mono.empty();
    }

    @DeleteMapping("/api/v1/book/{id}")
    public Mono<Void> deleteBook(@PathVariable("id") ObjectId id) {
        return bookRepository.deleteById(id);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handleNotFound(EntityNotFoundException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }
}