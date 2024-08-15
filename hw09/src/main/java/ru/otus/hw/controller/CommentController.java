package ru.otus.hw.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.otus.hw.exception.EntityNotFoundException;
import ru.otus.hw.service.CommentService;

@RequiredArgsConstructor
@Controller
public class CommentController {
    private final CommentService commentService;

    @GetMapping("/comment")
    public String readAllCommentsByBookId(@RequestParam("bookId") long bookId, Model model) {
        var comments = commentService.findAllCommentsByBookId(bookId);
        model.addAttribute("comments", comments);
        model.addAttribute("bookId", bookId);
        return "allCommentsByBook";
    }

    @GetMapping("/comment/{id}")
    public String readComment(@PathVariable("id") long id, Model model) {
        var comment = commentService.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Comment with id %d not found".formatted(id)));
        model.addAttribute("comment", comment);
        return "singleComment";
    }

    @PostMapping("/comment")
    public String updateComment(@RequestParam("id") long id,
                                @RequestParam("description") String description) {
        var updatedComment = commentService.update(id, description);
        String path = "redirect:/comment?bookId=" + updatedComment.getBookId();
        return path;
    }

    @GetMapping("/comment/new")
    public String addComment(@RequestParam("bookId") long bookId, Model model) {
        model.addAttribute("bookId", bookId);
        return "addComment";
    }

    @PostMapping("/comment/new")
    public String addComment(@RequestParam("bookId") long bookId,
                              @RequestParam("description") String description) {
        commentService.insert(description, bookId);
        String path = "redirect:/comment?bookId=" + bookId;
        return path;
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handleNotFound(EntityNotFoundException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }
}
