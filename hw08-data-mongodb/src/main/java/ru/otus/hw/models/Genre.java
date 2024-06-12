package ru.otus.hw.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import ru.otus.hw.models.prototype.Copyable;

@Document(collection = "genres")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Genre implements Copyable<Genre> {
    @Id
    private long id;

    private String name;

    public Genre(String name) {
        this.name = name;
    }

    @Override
    public Genre copy() {
        return new Genre(this.getId(), this.getName());
    }
}
