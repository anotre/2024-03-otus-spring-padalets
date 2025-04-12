package ru.otus.hw;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
		System.out.printf("REST: %n%s%n",
				"http://localhost:8080/api/v1");
		System.out.printf("HATEOAS: %n%s%n",
				"http://localhost:8080/api/v2");
	}
}
