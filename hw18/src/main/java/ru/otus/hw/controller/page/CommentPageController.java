package ru.otus.hw.controller.page;

import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RequiredArgsConstructor
@Controller
public class CommentPageController {
    @GetMapping("/comment")
    public String readAllCommentsByBookId(@RequestParam("bookId") ObjectId bookId, Model model) {
        model.addAttribute("bookId", bookId);
        return "allCommentsByBook";
    }

    @GetMapping("/comment/{id}")
    public String readComment(@PathVariable("id") ObjectId id) {
        return "singleComment";
    }

    @GetMapping("/comment/new")
    public String addComment(@RequestParam("bookId") ObjectId bookId, Model model) {
        model.addAttribute("bookId", bookId);
        return "addComment";
    }

    @GetMapping("/comment/{id}/del")
    public String deleteComment(@PathVariable("id") ObjectId id) {
        return "deleteComment";
    }
}
