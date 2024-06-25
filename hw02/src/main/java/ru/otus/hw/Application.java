package ru.otus.hw;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import ru.otus.hw.service.TestRunnerService;
import ru.otus.hw.service.TestRunnerServiceImpl;

@ComponentScan
public class Application {
    public static void main(String[] args) {

        ApplicationContext context = new AnnotationConfigApplicationContext(Application.class);
        TestRunnerService testRunnerService = context.getBean(TestRunnerServiceImpl.class);
        testRunnerService.run();

    }
}