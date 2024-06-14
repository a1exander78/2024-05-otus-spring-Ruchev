package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import ru.otus.hw.dao.QuestionDao;

@RequiredArgsConstructor
public class TestServiceImpl implements TestService {

    private final IOService iOService;

    private final QuestionDao questionDao;

    @Override
    public void executeTest() {
        var questions = questionDao.findAll();

        for (int i = 0; i < questions.size(); i++) {

            int numberOfQuestion = i + 1;
            var currentQuestionText = questions.get(i).getQuestionText();

            iOService.printFormattedLine("%s" + currentQuestionText, "\nQuestion #" + numberOfQuestion + ": ");

            var answers = questions.get(i).answers();

            for (int j = 0; j < answers.size(); j++) {

                int numberOfAnswer = j + 1;
                var currentAnswerText = answers.get(j).getAnswerText();
                var currentAnswerCorrectness = answers.get(j).getAnswerCorrectness();


                iOService.printFormattedLine("%s" + currentAnswerText, "Answer #" + numberOfAnswer + ": ");
                iOService.printLine(currentAnswerCorrectness);

            }
        }
    }
}
