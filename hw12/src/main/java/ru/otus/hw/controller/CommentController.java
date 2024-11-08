package ru.otus.hw.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import ru.otus.hw.dto.CommentDtoRequest;
import ru.otus.hw.exception.EntityNotFoundException;
import ru.otus.hw.model.User;
import ru.otus.hw.service.BookService;
import ru.otus.hw.service.CommentService;
import ru.otus.hw.service.UserService;

@RequiredArgsConstructor
@Controller
public class CommentController {
    private final CommentService commentService;

    private final BookService bookService;

    private final UserService userService;

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
        model.addAttribute("comment", comment);
        model.addAttribute("bookId", comment.getBookId());
        return "singleComment";
    }

    @PostMapping("/comment/{id}")
    public String updateComment(@Valid @ModelAttribute("comment") CommentDtoRequest comment,
                                BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("bookId", commentService.findById(comment.getId()).get().getBookId());
            return "singleComment";
        }
        var updatedComment = commentService.update(comment.getId(), comment.getDescription());
        String path = "redirect:/comment?bookId=" + updatedComment.getBookId();
        return path;
    }

    @GetMapping("/comment/new")
    public String addComment(@RequestParam("bookId") long bookId, Model model) {
        model.addAttribute("comment", new CommentDtoRequest());
        model.addAttribute("bookId", bookId);
        return "addComment";
    }

    @PostMapping("/comment/new")
    public String addComment(@Valid @ModelAttribute("comment") CommentDtoRequest comment,
                             BindingResult bindingResult, Model model) {
        var description = comment.getDescription();
        var bookId = comment.getBookId();

        var securityContext = SecurityContextHolder.getContext();

        var userId = getCurrentUser(securityContext.getAuthentication()).getId();
        if (bindingResult.hasErrors()) {
            model.addAttribute("bookId", bookId);
            model.addAttribute("userId", userId);
            return "addComment";
        }
        commentService.insert(description, bookId, userId);
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
        return "redirect:/book/";
    }

    private User getCurrentUser(Authentication authentication) {
        var user = (UserDetails) authentication.getPrincipal();
        return userService.findByLogin(user.getUsername());
    }
}
