package ru.otus.hw.dao;

import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.enums.CSVReaderNullFieldIndicator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.hw.config.TestFileNameProvider;
import ru.otus.hw.dao.dto.QuestionDto;
import ru.otus.hw.domain.Question;
import ru.otus.hw.exceptions.QuestionReadException;
import ru.otus.hw.reader.ResourceReader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CsvQuestionDao implements QuestionDao {
    private final TestFileNameProvider fileNameProvider;

    private final ResourceReader resourceReader;

    @Override
    public List<Question> findAll() {
        var filename = this.fileNameProvider.getTestFileName();
        try (var reader = new BufferedReader(new InputStreamReader(this.resourceReader.getResourceAsStream(filename)))) {
            return new CsvToBeanBuilder<QuestionDto>(reader)
                    .withType(QuestionDto.class)
                    .withSeparator(';')
                    .withSkipLines(1)
                    .withFieldAsNull(CSVReaderNullFieldIndicator.EMPTY_SEPARATORS)
                    .build()
                    .parse()
                    .stream()
                    .map(QuestionDto::toDomainObject).toList();
        } catch (RuntimeException | IOException exception) {
            throw new QuestionReadException("Error occurred while data receiving.", exception);
        }
    }
}
