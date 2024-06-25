package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.converters.QuestionToStringConverter;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Question;
import ru.otus.hw.domain.Student;
import ru.otus.hw.domain.TestResult;

@Service
@RequiredArgsConstructor
public class TestServiceImpl implements TestService {

    private final IOService ioService;

    private final QuestionDao questionDao;

    private final QuestionToStringConverter questionToStringConverter;

    @Override
    public void executeTest() {
        var questions = questionDao.findAll();
        for (Question currentQuestion : questions) {
            ioService.printLine(questionToStringConverter.convertQuestionToString(currentQuestion));
        }
    }

    @Override
    public TestResult executeTestFor(Student student) {
        ioService.printLine("");
        ioService.printFormattedLine("Please answer the questions below%n");
        var questions = questionDao.findAll();
        var testResult = new TestResult(student);

        for (var question: questions) {
            var isAnswerValid = false; // Задать вопрос, получить ответ
            var answers = question.answers();
            var numberOfAnswer = ioService.readIntForRangeWithPrompt(1, answers.size(),
                    questionToStringConverter.convertQuestionToString(question), "Invalid answer");
            if (answers.get(numberOfAnswer - 1).isCorrect()) {
                isAnswerValid = true;
            }
            testResult.applyAnswer(question, isAnswerValid);
        }
        return testResult;
    }
}
