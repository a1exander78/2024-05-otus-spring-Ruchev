package ru.otus.hw.converters;

import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;

public class QuestionToStringConverter {

    public String convertQuestionToString(Question question) {
        var stringBuilder = new StringBuilder();
        var answers = question.answers().stream().map(Answer::text).toList();
        var questionWithAnswers = stringBuilder.append(question.text()).append("\n");
        for (int i = 0; i < answers.size(); i++) {
            char letterOfAnswer = (char) (65 + i);
            questionWithAnswers.append(letterOfAnswer).append(") ").append(answers.get(i)).append("\t\t\t");
        }
        return questionWithAnswers.append("\n").toString();
    }
}
