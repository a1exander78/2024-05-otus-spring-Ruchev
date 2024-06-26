package ru.otus.hw.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.hw.config.AppProperties;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Class CsvQuestionDao")
class CsvQuestionDaoTest {

    private AppProperties properties;
    private CsvQuestionDao csvQuestionDao;

    @BeforeEach
    void setUp() {
        properties = new AppProperties(3, "/questions.csv");
        csvQuestionDao = new CsvQuestionDao(properties);
    }

    @DisplayName("should read question from file correctly")
    @Test
    void should() {
        var question = csvQuestionDao.findAll().get(0);
        var answers = question.answers();
        assertThat(question.text()).isEqualTo("Which element of the periodic table is designated as \"Ag\"?");
    }
}