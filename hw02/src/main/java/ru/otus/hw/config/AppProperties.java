package ru.otus.hw.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:application.properties")
@Data
public class AppProperties implements TestConfig, TestFileNameProvider {

    // внедрить свойство из application.properties
    private int rightAnswersCountToPass;

    // внедрить свойство из application.properties
    private String testFileName;

    public AppProperties(@Value("${test.rightAnswersCountToPass}") int rightAnswersCountToPass, @Value("${test.fileName}") String testFileName) {
        this.rightAnswersCountToPass = rightAnswersCountToPass;
        this.testFileName = testFileName;
    }


}
