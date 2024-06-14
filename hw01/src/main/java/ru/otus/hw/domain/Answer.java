package ru.otus.hw.domain;

public record Answer(String text, boolean isCorrect) {

    public String getAnswerText() {
        return text();
    }

    public String getAnswerCorrectness() {
        return isCorrect() == true ? "(Correct answer)" : "(Incorrect answer)";
    }
}
