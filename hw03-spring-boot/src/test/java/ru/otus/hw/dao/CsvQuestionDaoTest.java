package ru.otus.hw.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.otus.hw.config.AppProperties;
import ru.otus.hw.domain.Question;
import ru.otus.hw.reader.ResourceReaderImpl;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatRuntimeException;

@DisplayName("СsvQuestionDao - читает вопросы и варианты ответов из csv файла")
class CsvQuestionDaoTest {
    private QuestionDao sut;

    private AppProperties testFileNameProvider;

    @BeforeEach
    void setUp() {
        var resourceReader = new ResourceReaderImpl();
        testFileNameProvider = Mockito.mock(AppProperties.class);
        Mockito.when(testFileNameProvider.getTestFileName()).thenReturn("questions.csv");
        Mockito.when(testFileNameProvider.getRightAnswersCountToPass()).thenReturn(3);
        sut = new CsvQuestionDao(testFileNameProvider, resourceReader);
    }

    @Test
    @DisplayName("Находит все доступные вопросы")
    void shouldFindExpectedQuestionNumber() {
        var expectedQuestionNumber = 3;
        assertThat(sut.findAll().size()).isEqualTo(expectedQuestionNumber);
    }

    @Test
    @DisplayName("Находит только перечисленные в файле вопросы")
    void shouldFindParticularExpectedQuestion() {
        List<String> expectedQuestions = List.of(
                "Is there life on Mars?",
                "How should resources be loaded form jar in Java?",
                "Which option is a good way to handle the exception?"
        );
        List<String> questions = sut.findAll().stream().map(Question::text).toList();
        assertThat(questions).containsAll(expectedQuestions);
    }

    @Test
    @DisplayName("Бросается исключение если встречается вопрос не имеющий ответов")
    void shouldThrowIllegalStateExceptionIfNoAnswers() {
        Mockito.when(testFileNameProvider.getTestFileName()).thenReturn("questions-incorrect.csv");
        assertThatRuntimeException().isThrownBy(() -> sut.findAll());
    }
}