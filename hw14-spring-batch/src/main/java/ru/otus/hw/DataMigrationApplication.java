package ru.otus.hw;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DataMigrationApplication {

    public static void main(String[] args) {
        SpringApplication.run(DataMigrationApplication.class, args);
        System.out.println("""
                Команда для старта миграции из MongoDB в реляционную БД: 'sm'
                Команда для старта миграции: 'rlfm'
                
                """);
    }

}

