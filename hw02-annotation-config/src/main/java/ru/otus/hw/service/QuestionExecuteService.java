package ru.otus.hw.service;

import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;

import java.util.List;

public interface QuestionExecuteService {
    void askQuestion(Question question);

    void giveOptions(List<Answer> answers);

    int askAnswer(Question question);

    boolean analyseAnswer(Question question, int receivedAnswer);
}
