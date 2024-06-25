package ru.otus.hw.dao;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.otus.hw.config.AppProperties;
import ru.otus.hw.config.TestFileNameProvider;
import ru.otus.hw.dao.dto.QuestionDto;
import ru.otus.hw.domain.Question;
import ru.otus.hw.exceptions.QuestionReadException;

import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CsvQuestionDao implements QuestionDao {

    private final AppProperties properties;

    @Override
    public List<Question> findAll() {

        try (Reader reader = new InputStreamReader(getClass().getResourceAsStream(properties.getTestFileName()))) {

            CsvToBean<QuestionDto> cb = new CsvToBeanBuilder<QuestionDto>(reader)
                    .withType(QuestionDto.class)
                    .withSkipLines(1)
                    .withSeparator(';')
                    .build();

            return cb.parse().stream().map(QuestionDto::toDomainObject).toList();

        } catch (Exception e) {
            throw new QuestionReadException("Error reading from file", e);
        }
    }
}
