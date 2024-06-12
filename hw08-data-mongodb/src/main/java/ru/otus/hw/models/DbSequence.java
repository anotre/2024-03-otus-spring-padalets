package ru.otus.hw.models;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "db_sequence")
@Getter
@Setter
public class DbSequence {
    @Id
    private String id;

    private Long sequence;
}
