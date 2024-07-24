package ru.otus.hw.services;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;
import ru.otus.hw.controllers.dto.converter.AuthorDtoConverter;
import ru.otus.hw.repositories.AuthorRepository;

@SpringBootTest
@DisplayName("Сервис для работы с сущностями авторов")
@Import({AuthorServiceImpl.class})
class AuthorServiceImplTest {
    @Autowired
    private AuthorService authorService;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private AuthorDtoConverter authorDtoConverter;

    @Test
    void shouldFindAllAuthors() {
        var expectedAuthors = authorRepository
                .findAll()
                .map(authorDtoConverter::toDto);
        var expectedAuthorsCount = 3;
        var actualAuthors = authorService.findAll();
        var stepVerifier = StepVerifier
                .create(Flux.zip(actualAuthors, expectedAuthors));

        for (int i = 0; i < expectedAuthorsCount; i++) {
            stepVerifier.assertNext(tuple -> Assertions.assertThat(tuple.getT1()).isEqualTo(tuple.getT2()));
        }
        stepVerifier
                .expectComplete()
                .verify();
    }
}