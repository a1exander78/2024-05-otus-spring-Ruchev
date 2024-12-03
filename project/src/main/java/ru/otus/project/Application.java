package ru.otus.project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import ru.otus.project.props.AppProps;
import ru.otus.project.props.SecurityProps;

@EnableConfigurationProperties(AppProps.class)
@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
		System.out.println("http://localhost:8080/h2-console/");
	}

}
