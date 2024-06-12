package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import ru.otus.hw.models.DbSequence;

import java.util.Objects;

import static org.springframework.data.mongodb.core.FindAndModifyOptions.options;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@Service
@RequiredArgsConstructor
public class SequenceGeneratorServiceServiceImpl implements SequenceGeneratorService {
    private final MongoOperations mongoOperations;

    private final String sequenceFieldName = "sequence";

    public long generateSequence(String sequenceName) {
        DbSequence sequence = mongoOperations.findAndModify(
                query(where("_id").is(sequenceName)),
                new Update().inc(sequenceFieldName, 1), options().returnNew(true).upsert(true),
                DbSequence.class);
        return !Objects.isNull(sequence) ? sequence.getSequence() : 1;
    }
}
