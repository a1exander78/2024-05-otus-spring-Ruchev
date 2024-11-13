package ru.otus.hw.controller.rest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ExceptionHandler;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.converter.dto.CommentDtoConverter;
import ru.otus.hw.dto.CommentDto;
import ru.otus.hw.dto.CommentDtoRequest;
import ru.otus.hw.exception.EntityNotFoundException;
import ru.otus.hw.model.Comment;
import ru.otus.hw.repository.reactive.BookRepository;
import ru.otus.hw.repository.reactive.CommentRepository;

@RequiredArgsConstructor
@RestController
public class CommentRestController {
    private final CommentRepository commentRepository;

    private final BookRepository bookRepository;

    private final CommentDtoConverter converter;

    @GetMapping("/api/v1/comment")
    public Flux<CommentDto> readAllCommentsByBookId(@RequestParam("bookId") ObjectId bookId) {
        return commentRepository.findAllCommentsByBookId(bookId).map(converter::toDto);
    }

    @GetMapping("/api/v1/comment/{id}")
    public Mono<ResponseEntity<CommentDto>> readBook(@PathVariable("id") ObjectId id) {
        return commentRepository.findById(id)
                .map(converter::toDto)
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.error(new EntityNotFoundException("Comment with id %s not found".formatted(id))));
    }

    @PutMapping("/api/v1/comment/{id}")
    public Mono<CommentDto> updateComment(@Valid @RequestBody CommentDtoRequest comment,
                             BindingResult bindingResult) {
        if (!bindingResult.hasErrors()) {
            var oldComment = commentRepository.findById(comment.getId()).block();
            return commentRepository.save(Comment.builder()
                    .id(oldComment.getId())
                    .description(comment.getDescription())
                    .book(oldComment.getBook())
                    .build())
                    .map(converter::toDto);
        }
        return Mono.empty();
    }

    @PostMapping("/api/v1/comment")
    public Mono<CommentDto> addComment(@Valid @RequestBody CommentDtoRequest comment,
                                    BindingResult bindingResult) {
        if (!bindingResult.hasErrors()) {
            return commentRepository.save(Comment.builder()
                    .id(new ObjectId())
                    .description(comment.getDescription())
                    .book(bookRepository.findById(comment.getBookId()).block())
                    .build())
                    .map(converter::toDto);
        }
        return Mono.empty();
    }

    @DeleteMapping("/api/v1/comment/{id}")
    public Mono<Void> deleteComment(@PathVariable("id") ObjectId id) {
        return commentRepository.deleteById(id);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handleNotFound(EntityNotFoundException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }
}