package ru.otus.hw.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@PropertySource("classpath:application.properties")
@Configuration
public class AppConfig {

    @Bean
    public AppProperties appProperties(@Value("${test.rightAnswersCountToPass}") int rightAnswersCountToPass,
                                       @Value("${test.fileName}") String testFileName) {
        return new AppProperties(rightAnswersCountToPass, testFileName);
    }
}
