package ru.otus.hw.controller.page;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RequiredArgsConstructor
@Controller
public class PageController {
    @GetMapping({"/", "/api/v1/main"})
    public String startPage() {
        return "start";
    }

    @GetMapping("/api/v1/book")
    public String readAllBooks() {
        return "allBooks";
    }

    @GetMapping("/api/v1/book/{id}")
    public String readBook(@PathVariable("id") long id) {
        return "singleBook";
    }

    @GetMapping("/api/v1/book/new")
    public String addBook() {
        return "addBook";
    }

    @GetMapping("/api/v1/book/{id}/del")
    public String deleteBook(@PathVariable("id") long id) {
        return "deleteBook";
    }
}