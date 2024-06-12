package ru.otus.hw.services;

import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.events.EntitySequenceNames;
import ru.otus.hw.models.DbSequence;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
@DisplayName("Сервис для генерации последовательности идентификаторов для сущностей")
@ComponentScan({"ru.otus.hw.services", "ru.otus.hw.events"})
@Transactional(propagation = Propagation.NEVER)
class SequenceGeneratorServiceServiceImplTest {

    @Autowired
    private MongoOperations mongoOperations;

    @Autowired
    private SequenceGeneratorService sequenceGenerator;

    @Test
    @DisplayName("Генерирует инкрементированное значение последовательности")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void shouldGenerateIncrementedIdValue() {
        val initialSequenceValue = mongoOperations.findById(EntitySequenceNames.GENRE, DbSequence.class);
        var expectedNewSequenceValue = initialSequenceValue.getSequence() + 1;
        var generatedSequenceValue = sequenceGenerator.generateSequence(EntitySequenceNames.GENRE);
        assertThat(generatedSequenceValue).isEqualTo(expectedNewSequenceValue);
    }
}