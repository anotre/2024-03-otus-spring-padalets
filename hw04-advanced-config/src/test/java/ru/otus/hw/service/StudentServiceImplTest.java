package ru.otus.hw.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.otus.hw.domain.Student;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

class StudentServiceImplTest {
    private StudentService sut;

    private final String testName = "John";

    private final String testLastName = "Doe";

    @BeforeEach
    void setUp() {
        LocalizedIOService ioService = Mockito.mock(LocalizedIOService.class);

        given(ioService.readStringWithPromptLocalized("StudentService.input.first.name")).willReturn(testName);
        given(ioService.readStringWithPromptLocalized("StudentService.input.last.name")).willReturn(testLastName);
        given(ioService.readStringWithPromptLocalized("")).willReturn("");
        sut = new StudentServiceImpl(ioService);
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