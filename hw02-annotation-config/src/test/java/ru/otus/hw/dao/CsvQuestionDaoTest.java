package ru.otus.hw.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.hw.config.AppProperties;
import ru.otus.hw.config.TestFileNameProvider;
import ru.otus.hw.domain.Question;
import ru.otus.hw.reader.ResourceReader;
import ru.otus.hw.reader.ResourceReaderImpl;

import java.io.IOException;
import java.util.List;
import java.util.Properties;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("СsvQuestionDao - читает вопросы и варианты ответов из csv файла")
class CsvQuestionDaoTest {
    private TestFileNameProvider testFileNameProvider;

    private ResourceReader resourceReader;

    private QuestionDao sut;

    private int rightAnswersCountToPass;

    private String fileName;

    @BeforeEach
    void setUp() throws IOException {
        var properties = new Properties();
        properties.load(ClassLoader.getSystemResourceAsStream("application-test.properties"));
        rightAnswersCountToPass = Integer.parseInt(properties.getProperty("test.rightAnswersCountToPass"));
        fileName = properties.getProperty("test.fileName");
        testFileNameProvider = new AppProperties(rightAnswersCountToPass, fileName);
        resourceReader = new ResourceReaderImpl();
        sut = new CsvQuestionDao(testFileNameProvider, resourceReader);
    }

    @Test
    @DisplayName("Находит все доступные вопросы")
    void shouldFindExpectedQuestionNumber() {
        var expectedQuestionNumber = 4;
        assertThat(sut.findAll().size()).isEqualTo(expectedQuestionNumber);
    }

    @Test
    @DisplayName("Находит только перечисленные в файле вопросы")
    void shouldFindParticularExpectedQuestion() {
        List<String> expectedQuestions = List.of(
                "Is there life on Mars?",
                "How should resources be loaded form jar in Java?",
                "Which option is a good way to handle the exception?",
                "To be or not to be?"
        );
        List<String> questions = sut.findAll().stream().map(Question::text).toList();
        assertThat(questions).containsAll(expectedQuestions);
    }
}