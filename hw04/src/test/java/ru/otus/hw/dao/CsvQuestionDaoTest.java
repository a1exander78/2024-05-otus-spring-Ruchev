package ru.otus.hw.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.otus.hw.config.AppProperties;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;


@DisplayName("Class CsvQuestionDao")
@SpringBootTest(classes = CsvQuestionDao.class)
class CsvQuestionDaoTest {

    @MockBean
    private AppProperties properties;

    @Autowired
    private CsvQuestionDao csvQuestionDao;

    private List<Question> expectedQuestionsList;

    @BeforeEach
    void setUp() {
        var answer1 = new Answer("Gold", false);
        var answer2 = new Answer("Argon", false);
        var answer3 = new Answer("Silver", true);

        var answers = List.of(answer1, answer2, answer3);

        expectedQuestionsList = List.of(new Question("Which element of the periodic table is designated as \"Ag\"?", answers));
    }

    @DisplayName("should read questions from file correctly")
    @Test
    void shouldReadQuestionsFromFileCorrectly() {
        given(properties.getTestFileName()).willReturn("/questions.csv");
        var list = csvQuestionDao.findAll();
        assertThat(list).isEqualTo(expectedQuestionsList);
    }
}