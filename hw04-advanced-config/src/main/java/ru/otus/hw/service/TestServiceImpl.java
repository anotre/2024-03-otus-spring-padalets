package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;
import ru.otus.hw.domain.Student;
import ru.otus.hw.domain.TestResult;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TestServiceImpl implements TestService {

    private final LocalizedIOService ioService;

    private final QuestionDao questionDao;

    @Override
    public TestResult executeTestFor(Student student) {
        ioService.printLine("");
        ioService.printLineLocalized("TestService.answer.the.questions");
        ioService.printLine("");

        var questions = questionDao.findAll();
        var testResult = new TestResult(student);

        for (var question: questions) {
            askQuestion(question);
            var answer = askAnswer(question);
            var isAnswerValid = analyseAnswer(question, answer);
            testResult.applyAnswer(question, isAnswerValid);
        }

        return testResult;
    }

    private void askQuestion(Question question) {
        ioService.printLine(question.text());
        giveOptions(question.answers());
    }

    private void giveOptions(List<Answer> answers) {
        if (answers.isEmpty()) {
            ioService.printFormattedLineLocalized("TestService.no.answer.variants");
            return;
        }

        for (int i = 0; i < answers.size(); i++) {
            ioService.printFormattedLine("\t%d) %s", i + 1, answers.get(i).text());
        }
    }

    private int askAnswer(Question question) {
        var min = 1;
        var max = question.answers().size();
        return ioService.readIntForRangeWithPromptLocalized(
                min,
                max,
                "TestService.answer.the.question",
                "TestService.incorrect.answer.message");
    }

    private boolean analyseAnswer(Question question, int receivedAnswer) {
        if (question.answers().size() >= receivedAnswer) {
            return question.answers().get(receivedAnswer - 1).isCorrect();
        }
        return false;
    }
}
