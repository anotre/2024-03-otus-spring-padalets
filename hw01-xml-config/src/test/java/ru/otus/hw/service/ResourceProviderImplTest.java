package ru.otus.hw.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;

@DisplayName("ResourceProviderImpl class under the test:")
class ResourceProviderImplTest {
    public static final String CORRECT_RESOURCE_NAME = "questions.csv";

    public static final String INCORRECT_RESOURCE_NAME = "non-exists.csv";
    private ResourceProvider sut;

    @BeforeEach
    void setUp() {
        sut = new ResourceProviderImpl();
    }

    @DisplayName("Получение ресурса на несуществующем файле должно вызвать IOException")
    @Test
    void shouldThrowIOExceptionIfResourceDoesntExists() {
        Assertions.assertThatIOException().isThrownBy(() -> sut.getBufferedReaderForResource(INCORRECT_RESOURCE_NAME));
    }

    @DisplayName("Получение ресурса на существующем файле не должно вызывать исключения")
    @Test
    void shouldNotThrowIOExceptionIfResourceExists() {
        Assertions.assertThatNoException().isThrownBy(() -> sut.getBufferedReaderForResource(CORRECT_RESOURCE_NAME));
    }

    @DisplayName("Получение ресурса если передано null, вместо имени ресурса - должно вызывать NPE")
    @Test
    void shouldThrowNullPointerExceptionIfResourceNameIsEmpty() {
        Assertions.assertThatNullPointerException().isThrownBy(() -> sut.getBufferedReaderForResource(null));
    }

    @DisplayName("Полученный результат расширяет класс BufferedReader")
    @Test
    void shouldBeInstanceOfBufferedReader() throws IOException {
        Assertions.assertThat(sut.getBufferedReaderForResource(CORRECT_RESOURCE_NAME))
                .isInstanceOf(BufferedReader.class);
    }

    @DisplayName("Полученный результат возвращает данные из запрощенного ресурса")
    @Test
    void shouldContainDataFromRequestedResource() throws IOException {
        var expectedString = "# Добавить сюда своих вопросов. Эту строку надо пропустить при настройке " +
                "CsvToBean (withSkipLines)";
        Assertions.assertThat(sut.getBufferedReaderForResource(CORRECT_RESOURCE_NAME).readLine())
                .isEqualTo(expectedString);
    }
}