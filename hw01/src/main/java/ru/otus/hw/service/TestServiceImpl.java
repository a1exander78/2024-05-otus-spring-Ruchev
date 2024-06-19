package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.dao.dto.AnswerPrinter;
import ru.otus.hw.dao.dto.QuestionPrinter;
import ru.otus.hw.domain.Question;

import java.util.List;

@RequiredArgsConstructor
public class TestServiceImpl implements TestService {

    private final QuestionDao questionDao;

    private final QuestionPrinter questionPrinter;

    private final AnswerPrinter answerPrinter;

    @Override
    public void executeTest() {
        printQuestionsAndAnswers(questionDao.findAll());
    }

    private void printQuestionsAndAnswers(List<Question> questions) {

        for (int i = 0; i < questions.size(); i++) {

            int numberOfQuestion = i + 1;
            var currentQuestion = questions.get(i);

            questionPrinter.printQuestionText(numberOfQuestion, currentQuestion);

            var answers = questions.get(i).answers();

            for (int j = 0; j < answers.size(); j++) {

                int numberOfAnswer = j + 1;
                var currentAnswer = answers.get(j);

                answerPrinter.printAnswerText(numberOfAnswer, currentAnswer);
                answerPrinter.printAnswerCorrectness(currentAnswer);
            }
        }
    }
}
