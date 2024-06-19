package ru.otus.hw.dao.dto;

import lombok.RequiredArgsConstructor;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.service.IOService;

@RequiredArgsConstructor
public class AnswerPrinter {

    private final IOService iOService;

    public void printAnswerText(int numberOfAnswer, Answer answer) {
        iOService.printFormattedLine("%s" + answer.text(), "Answer #" + numberOfAnswer + ": ");
    }

    public void printAnswerCorrectness(Answer answer) {
        iOService.printLine(answer.isCorrect() ? "(Correct answer)" : "(Incorrect answer)");
    }
}
