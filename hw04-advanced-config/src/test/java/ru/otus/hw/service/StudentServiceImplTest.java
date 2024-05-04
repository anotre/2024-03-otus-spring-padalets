package ru.otus.hw.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.otus.hw.domain.Student;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@SpringBootTest
class StudentServiceImplTest {
    @Autowired
    private StudentService sut;

    @MockBean
    private LocalizedIOService ioService;

    private final String testName = "John";

    private final String testLastName = "Doe";



    @BeforeEach
    void setUp() {
        given(ioService.readStringWithPromptLocalized("StudentService.input.first.name")).willReturn(testName);
        given(ioService.readStringWithPromptLocalized("StudentService.input.last.name")).willReturn(testLastName);
        given(ioService.readStringWithPromptLocalized("")).willReturn("");
    }

    @Test
    @DisplayName("Возвращает экземпляр класса Student")
    void shouldReturnInstanceOfStudent() {
        assertThat(sut.determineCurrentStudent()).isInstanceOf(Student.class);
    }

    @Test
    @DisplayName("Возвращает новый экземпляр студента на основе ввода пользователя")
    void shouldReturnParticularStudent() {
        assertThat(sut.determineCurrentStudent().firstName()).isEqualTo(testName);
        assertThat(sut.determineCurrentStudent().lastName()).isEqualTo(testLastName);
    }
}