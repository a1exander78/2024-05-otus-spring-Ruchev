package ru.otus.hw.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import ru.otus.hw.dto.CommentDtoRequest;
import ru.otus.hw.exception.EntityNotFoundException;
import ru.otus.hw.service.BookService;
import ru.otus.hw.service.CommentService;

@RequiredArgsConstructor
@Controller
public class CommentController {
    private final CommentService commentService;

    private final BookService bookService;

    @GetMapping("/api/v1/comment")
    public String readAllCommentsByBookId(@RequestParam("bookId") long bookId, Model model) {
        var comments = commentService.findAllCommentsByBookId(bookId);
        var book = bookService.findById(bookId).get();
        model.addAttribute("comments", comments);
        model.addAttribute("book", book);
        return "allCommentsByBook";
    }

    @GetMapping("/api/v1/comment/{id}")
    public String readComment(@PathVariable("id") long id, Model model) {
        var comment = commentService.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Comment with id %d not found".formatted(id)));
        model.addAttribute("comment", comment);
        model.addAttribute("bookId", comment.getBookId());
        return "singleComment";
    }

    @PostMapping("/api/v1/comment/{id}")
    public String updateComment(@Valid @ModelAttribute("comment") CommentDtoRequest comment,
                                BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("bookId", commentService.findById(comment.getId()).get().getBookId());
            return "singleComment";
        }
        var updatedComment = commentService.update(comment.getId(), comment.getDescription());
        String path = "redirect:/api/v1/comment?bookId=" + updatedComment.getBookId();
        return path;
    }

    @GetMapping("/api/v1/comment/new")
    public String addComment(@RequestParam("bookId") long bookId, Model model) {
        model.addAttribute("comment", new CommentDtoRequest());
        model.addAttribute("bookId", bookId);
        return "addComment";
    }

    @PostMapping("/api/v1/comment/new")
    public String addComment(@Valid @ModelAttribute("comment") CommentDtoRequest comment,
                             BindingResult bindingResult, Model model) {
        var description = comment.getDescription();
        var bookId = comment.getBookId();
        if (bindingResult.hasErrors()) {
            model.addAttribute("bookId", bookId);
            return "addComment";
        }
        commentService.insert(description, bookId);
        System.out.println(comment);
        String path = "redirect:/api/v1/comment?bookId=" + bookId;
        return path;
    }

    @GetMapping("/api/v1/comment/{id}/del")
    public String deleteComment(@PathVariable("id") long id, Model model) {
        var comment = commentService.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Comment with id %d not found".formatted(id)));
        model.addAttribute("comment", comment);
        return "deleteComment";
    }

    @PostMapping("/api/v1/comment/{id}/del")
    public String deleteComment(@PathVariable("id") long id) {
        commentService.deleteById(id);
        return "redirect:/api/v1/book";
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ModelAndView handleNotFound(EntityNotFoundException ex) {
        ModelAndView modelAndView = new ModelAndView("error");
        modelAndView.addObject("message", ex.getMessage());
        return modelAndView;
    }
}
