package ru.otus.hw;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.shell.command.annotation.CommandScan;
import ru.otus.hw.config.AppProperties;

@SpringBootApplication
@EnableConfigurationProperties(AppProperties.class)
@CommandScan
public class Application {

    public static void main(String[] args) {
        //Создать контекст Spring Boot приложения
        SpringApplication.run(Application.class, args);
    }
}