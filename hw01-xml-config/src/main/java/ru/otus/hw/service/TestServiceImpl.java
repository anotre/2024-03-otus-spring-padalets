package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import ru.otus.hw.dao.CsvQuestionDao;
import ru.otus.hw.domain.Question;

@RequiredArgsConstructor
public class TestServiceImpl implements TestService {

    private final IOService ioService;

    private final CsvQuestionDao dao;

    @Override
    public void executeTest() {
        ioService.printLine("");
        ioService.printFormattedLine("Please answer the questions below%n");
        var questions = dao.findAll();

        for (int i = 0; i < questions.size(); i++) {
            outputQuestion(questions.get(i), i + 1);
        }
    }

    private void outputQuestion(Question question, int orderIndex) {
        ioService.printFormattedLine("%d) %s:", orderIndex, question.text());
        var answers = question.answers();

        if (answers.isEmpty()) {
            ioService.printFormattedLine("\tThere are no answer variants");
            return;
        }

        for (int i = 0; i < answers.size(); i++) {
            ioService.printFormattedLine("\t%c) %s", 'a' + i, answers.get(i).text());
        }
    }
}