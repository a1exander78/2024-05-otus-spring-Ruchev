package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.converters.QuestionToStringConverter;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Question;
import ru.otus.hw.domain.Student;
import ru.otus.hw.domain.TestResult;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TestServiceImpl implements TestService {

    private final LocalizedIOService ioService;

    private final QuestionDao questionDao;

    private final QuestionToStringConverter questionToStringConverter;

    @Override
    public TestResult executeTestFor(Student student) {
        ioService.printLineLocalized("TestService.answer.the.questions");
        return getTestResult(questionDao.findAll(), student);
    }

    private TestResult getTestResult(List<Question> questions, Student student) {
        var testResult = new TestResult(student);
        for (var question: questions) {
            var isAnswerValid = false; // Задать вопрос, получить ответ
            var answers = question.answers();
            var numberOfAnswer = ioService.readIntForRangeWithPromptLocalized(1, answers.size(),
                    "TestService.answer.the.question", "TestService.answer.invalid",
                    questionToStringConverter.convertQuestionToString(question));
            if (answers.get(numberOfAnswer - 1).isCorrect()) {
                isAnswerValid = true;
            }
            testResult.applyAnswer(question, isAnswerValid);
        }
        return testResult;
    }
}
