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
import ru.otus.hw.dto.CommentDto;
import ru.otus.hw.dto.CommentDtoRequest;
import ru.otus.hw.exception.EntityNotFoundException;
import ru.otus.hw.service.CommentService;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class CommentRestController {
    private final CommentService commentService;

    @GetMapping("/api/v1/comment")
    public List<CommentDto> readAllCommentsByBookId(@RequestParam("bookId") ObjectId bookId) {
        return commentService.findAllCommentsByBookId(bookId);
    }

    @GetMapping("/api/v1/comment/{id}")
    public CommentDto readComment(@PathVariable("id") ObjectId id) {
        var comment = commentService.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Book with id %d not found".formatted(id)));
        return comment;
    }

    @PutMapping("/api/v1/comment/{id}")
    public void updateComment(@Valid @RequestBody CommentDtoRequest comment,
                             BindingResult bindingResult) {
        if (!bindingResult.hasErrors()) {
            commentService.update(comment.getId(), comment.getDescription());
        }
    }

    @PostMapping("/api/v1/comment")
    public void addComment(@Valid @RequestBody CommentDtoRequest comment,
                           BindingResult bindingResult) {
        if (!bindingResult.hasErrors()) {
            commentService.insert(comment.getDescription(), comment.getBookId());
        }
    }

    @DeleteMapping("/api/v1/comment/{id}")
    public void deleteComment(@PathVariable("id") ObjectId id) {
        commentService.deleteById(id);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handleNotFound(EntityNotFoundException ex) {
        return ResponseEntity.badRequest().body("error");
    }
}