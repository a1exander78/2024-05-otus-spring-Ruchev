package ru.otus.hw.converters;

import org.springframework.stereotype.Component;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;

@Component
public class QuestionToStringConverter {

    public String convertQuestionToString(Question question) {
        var stringBuilder = new StringBuilder();
        var answers = question.answers().stream().map(Answer::text).toList();
        var questionWithAnswers = stringBuilder.append(question.text()).append("\n");
        for (int i = 0; i < answers.size(); i++) {
            int numberOfAnswer = i + 1;
            questionWithAnswers.append(numberOfAnswer).append(") ").append(answers.get(i)).append("\t\t\t");
        }
        return questionWithAnswers.toString();
    }
}
