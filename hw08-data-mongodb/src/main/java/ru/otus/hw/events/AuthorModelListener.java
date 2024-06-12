package ru.otus.hw.events;

import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.stereotype.Component;
import ru.otus.hw.models.Author;
import ru.otus.hw.services.SequenceGeneratorService;

@Component
@RequiredArgsConstructor
public class AuthorModelListener extends AbstractMongoEventListener<Author> {
    private final SequenceGeneratorService sequenceGenerator;

    @Override
    public void onBeforeConvert(BeforeConvertEvent<Author> event) {
        if (event.getSource().getId() <= 0) {
            event.getSource().setId(sequenceGenerator.generateSequence(EntitySequenceNames.AUTHOR));
        }
    }
}
