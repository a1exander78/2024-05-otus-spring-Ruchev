package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import ru.otus.hw.converters.QuestionToStringConverter;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Question;

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
}
