package ru.otus.hw.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.otus.hw.dto.CommentDtoRequest;
import ru.otus.hw.exception.EntityNotFoundException;
import ru.otus.hw.service.BookService;
import ru.otus.hw.service.CommentService;

@RequiredArgsConstructor
@Controller
public class CommentController {
    private final CommentService commentService;

    private final BookService bookService;

    @GetMapping("/comment")
    public String readAllCommentsByBookId(@RequestParam("bookId") long bookId, Model model) {
        var comments = commentService.findAllCommentsByBookId(bookId);
        var book = bookService.findById(bookId).get();
        model.addAttribute("comments", comments);
        model.addAttribute("book", book);
        return "allCommentsByBook";
    }

    @GetMapping("/comment/{id}")
    public String readComment(@PathVariable("id") long id, Model model) {
        var comment = commentService.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Comment with id %d not found".formatted(id)));

        if (!model.containsAttribute("comment")) {
            model.addAttribute("comment", comment);
        } else {
            var commentWithError = (CommentDtoRequest) model.getAttribute("comment");
            commentWithError.setDescription(comment.getDescription());
            model.addAttribute("comment", commentWithError);
        }

        return "singleComment";
    }

    @PostMapping("/comment/{id}")
    public String updateComment(@Valid @ModelAttribute("comment") CommentDtoRequest comment,
                                BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectRequestWithError(comment, bindingResult, redirectAttributes);
            return "redirect:/comment/" + comment.getId();
        }
        var updatedComment = commentService.update(comment.getId(), comment.getDescription());
        String path = "redirect:/comment?bookId=" + updatedComment.getBookId();
        return path;
    }

    @GetMapping("/comment/new")
    public String addComment(@RequestParam("bookId") long bookId, Model model) {
        if (!model.containsAttribute("comment")) {
            model.addAttribute("comment", new CommentDtoRequest());
        }
        model.addAttribute("bookId", bookId);
        return "addComment";
    }

    @PostMapping("/comment/new")
    public String addComment(@Valid @ModelAttribute("comment") CommentDtoRequest comment,
                             BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectRequestWithError(comment, bindingResult, redirectAttributes);
            return "redirect:/comment/new?bookId=" + comment.getBookId();
        }
        var description = comment.getDescription();
        var bookId = comment.getBookId();

        commentService.insert(description, bookId);
        System.out.println(comment);
        String path = "redirect:/comment?bookId=" + bookId;
        return path;
    }

    @GetMapping("/comment/{id}/del")
    public String deleteComment(@PathVariable("id") long id, Model model) {
        var comment = commentService.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Comment with id %d not found".formatted(id)));
        model.addAttribute("comment", comment);
        return "deleteComment";
    }

    @PostMapping("/comment/{id}/del")
    public String deleteComment(@PathVariable("id") long id) {
        commentService.deleteById(id);
        return "redirect:/book";
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handleNotFound(EntityNotFoundException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    private void redirectRequestWithError(CommentDtoRequest commentDtoRequest,
                                          BindingResult bindingResult,
                                          RedirectAttributes redirectAttributes) {
        redirectAttributes.getFlashAttributes().clear();
        redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.comment", bindingResult);
        redirectAttributes.addFlashAttribute("comment", commentDtoRequest);
    }
}
