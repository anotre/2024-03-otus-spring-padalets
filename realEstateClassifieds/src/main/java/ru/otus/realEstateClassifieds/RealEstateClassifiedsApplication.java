package ru.otus.realEstateClassifieds;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RealEstateClassifiedsApplication {

	public static void main(String[] args) {
		SpringApplication.run(RealEstateClassifiedsApplication.class, args);
		System.out.printf("Чтобы перейти на страницу swagger: %n%s%n",
				"http://localhost:8080/swagger-ui/index.html#/");
	}

}
