package ru.otus.hw.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import ru.otus.hw.models.prototype.Copyable;

@Document(collection = "authors")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Author implements Copyable<Author> {
    @Id
    private String id;

    private String fullName;

    public Author(String fullName) {
        this.fullName = fullName;
    }

    @Override
    public Author copy() {
        return new Author(this.getId(), this.getFullName());
    }
}
