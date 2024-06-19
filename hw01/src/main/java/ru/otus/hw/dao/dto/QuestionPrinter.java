package ru.otus.hw.dao.dto;

import lombok.RequiredArgsConstructor;
import ru.otus.hw.domain.Question;
import ru.otus.hw.service.IOService;

@RequiredArgsConstructor
public class QuestionPrinter {

    private final IOService iOService;

    public void printQuestionText(int numberOfQuestion, Question question) {
        iOService.printFormattedLine("%s" + question.text() + "\n", "\nQuestion #" + numberOfQuestion + ": ");
    }
}
