package ru.otus.hw.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

@DisplayName("Testing StreamsIOService class")
class StreamsIOServiceTest {
    private ByteArrayOutputStream captor;

    private StreamsIOService ioService;

    @BeforeEach
    void setUp() {
        this.captor = new ByteArrayOutputStream();
        this.ioService = new StreamsIOService(new PrintStream(this.captor));
    }

    @DisplayName("Корректность вывода метода printLine")
    @Test
    void printLineShouldPrintTheSameStringAsArgument() {
        String expectedString = "expected string";
        ioService.printLine(expectedString);
        Assertions.assertThat(captor.toString().trim()).isEqualTo(expectedString);
    }

    @DisplayName("Корректность вывода метода printFormattedLine")
    @Test
    void printFormattedLineShouldPrintTheSameStringAsArgument() {
        String expectedString = "expected string";
        ioService.printFormattedLine("expected %s", "string");
        Assertions.assertThat(captor.toString().trim()).isEqualTo(expectedString);
    }
}