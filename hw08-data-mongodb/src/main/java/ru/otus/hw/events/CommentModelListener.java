package ru.otus.hw.events;

import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.stereotype.Component;
import ru.otus.hw.models.Comment;
import ru.otus.hw.services.SequenceGeneratorService;

@Component
@RequiredArgsConstructor
public class CommentModelListener extends AbstractMongoEventListener<Comment> {
    private final SequenceGeneratorService sequenceGenerator;

    @Override
    public void onBeforeConvert(BeforeConvertEvent<Comment> event) {
        if (event.getSource().getId() <= 0) {
            event.getSource().setId(sequenceGenerator.generateSequence(EntitySequenceNames.COMMENT));
        }
    }
}
