package ru.otus.hw;

import com.github.cloudyrock.spring.v5.EnableMongock;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@EnableMongock
@EnableMongoRepositories(basePackages = "ru.otus.hw.repository")
@SpringBootApplication
public class Application {

	//запустить MongoDB
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
