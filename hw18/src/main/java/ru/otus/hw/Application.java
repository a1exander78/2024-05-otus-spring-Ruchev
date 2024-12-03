package ru.otus.hw;

import com.github.cloudyrock.spring.v5.EnableMongock;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@EnableMongock
@EnableMongoRepositories(basePackages = "ru.otus.hw.repository.classic")
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class Application {

	//запустить MongoDB
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
		System.out.printf("Чтобы перейти на страницу сайта открывай: %n%s%n",
				"http://localhost:8080");
	}

}