package ru.otus.hw.controller.page;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RequiredArgsConstructor
@Controller
public class BookPageController {
    @GetMapping("/book/")
    public String readAllBooks() {
        return "allBooks";
    }

    @GetMapping("/book/{id}")
    public String readBook(@PathVariable("id") long id) {
        return "singleBook";
    }

    @GetMapping("/book/new")
    public String addBook() {
        return "addBook";
    }

    @GetMapping("/book/{id}/del")
    public String deleteBook(@PathVariable("id") long id) {
        return "deleteBook";
    }
}