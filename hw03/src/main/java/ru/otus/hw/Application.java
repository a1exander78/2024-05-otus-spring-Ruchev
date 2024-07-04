package ru.otus.hw;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import ru.otus.hw.config.AppProperties;
import ru.otus.hw.service.TestRunnerService;

@SpringBootApplication
@EnableConfigurationProperties(AppProperties.class)
public class Application {

    public static void main(String[] args) {

        //Создать контекст Spring Boot приложения
        var context = SpringApplication.run(Application.class, args);
        var testRunnerService = context.getBean(TestRunnerService.class);
        testRunnerService.run();
    }
}