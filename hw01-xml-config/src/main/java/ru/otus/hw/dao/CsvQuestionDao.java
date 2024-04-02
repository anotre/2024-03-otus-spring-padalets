package ru.otus.hw.dao;

import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.enums.CSVReaderNullFieldIndicator;
import lombok.RequiredArgsConstructor;
import ru.otus.hw.config.TestFileNameProvider;
import ru.otus.hw.dao.dto.QuestionDto;
import ru.otus.hw.domain.Question;
import ru.otus.hw.exceptions.QuestionReadException;
import ru.otus.hw.config.ResourceProvider;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
public class CsvQuestionDao implements QuestionDao {
    private final TestFileNameProvider fileNameProvider;

    private final ResourceProvider resourceProvider;

    @Override
    public List<Question> findAll() {
        try (var reader = this.resourceProvider.getBufferedReaderForResource(this.fileNameProvider.getTestFileName())) {
            return new CsvToBeanBuilder<QuestionDto>(reader)
                    .withType(QuestionDto.class)
                    .withSeparator(';')
                    .withSkipLines(1)
                    .withFieldAsNull(CSVReaderNullFieldIndicator.EMPTY_SEPARATORS)
                    .build()
                    .parse()
                    .stream()
                    .map(QuestionDto::toDomainObject).toList();
        } catch (IllegalStateException | IOException exception) {
            throw new QuestionReadException("Error occurred while data receiving.", exception);
        }
    }
}
