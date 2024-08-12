package ru.otus.hw.command;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.hw.converter.toString.CommentDtoToStringConverter;
import ru.otus.hw.service.CommentService;

import java.util.stream.Collectors;

@SuppressWarnings({"SpellCheckingInspection", "unused"})
@RequiredArgsConstructor
@ShellComponent
public class CommentCommands {
    private final CommentService commentService;

    private final CommentDtoToStringConverter commentDtoToStringConverter;

    @ShellMethod(value = "Find all comments by Book", key = "acbb")
    public String findAllCommentsByBook(String bookId) {
        return commentService.findAllCommentsByBookId(bookId).stream()
                .map(commentDtoToStringConverter::commentDtoToString)
                .collect(Collectors.joining("," + System.lineSeparator()));
    }

    @ShellMethod(value = "Find comment by id", key = "cbid")
    public String findCommentById(String id) {
        return commentService.findById(id)
                .map(commentDtoToStringConverter::commentDtoToString)
                .orElse("Comment with id %s not found".formatted(id));
    }

    // cins newComment 2
    @ShellMethod(value = "Insert comment", key = "cins")
    public String insertComment(String description, String bookId) {
        var savedComment = commentService.insert(description, bookId);
        return commentDtoToStringConverter.commentDtoToString(savedComment);
    }

    // cupd 4 editedComment
    @ShellMethod(value = "Update comment", key = "cupd")
    public String updateComment(String id, String description) {
        var savedComment = commentService.update(id, description);
        return commentDtoToStringConverter.commentDtoToString(savedComment);
    }

    // cdel 4
    @ShellMethod(value = "Delete comment by id", key = "cdel")
    public void deleteComment(String id) {
        commentService.deleteById(id);
    }
}
