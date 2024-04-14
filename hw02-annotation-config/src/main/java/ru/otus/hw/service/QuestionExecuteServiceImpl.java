package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;

import java.util.List;

@Service
@RequiredArgsConstructor
public class QuestionExecuteServiceImpl implements QuestionExecuteService {
    private final IOService ioService;

    @Override
    public void askQuestion(Question question) {
        ioService.printLine(question.text());
    }

    @Override
    public void giveOptions(List<Answer> answers) {
        if (answers.isEmpty()) {
            ioService.printFormattedLine("\tThere are no answer variants");
            return;
        }

        for (int i = 0; i < answers.size(); i++) {
            ioService.printFormattedLine("\t%d) %s", i + 1, answers.get(i).text());
        }
    }

    @Override
    public int askAnswer(Question question) {
        var min = 1;
        var max = question.answers().size();
        return ioService.readIntForRangeWithPrompt(
                min,
                max,
                "Enter your answer:",
                "The answer must contain only numbers.");
    }

    @Override
    public boolean analyseAnswer(Question question, int receivedAnswer) {
        if (question.answers().size() >= receivedAnswer) {
            return question.answers().get(receivedAnswer - 1).isCorrect();
        }
        return false;
    }
}