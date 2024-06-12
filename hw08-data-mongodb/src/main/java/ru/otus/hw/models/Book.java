package ru.otus.hw.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import ru.otus.hw.models.prototype.Copyable;

@Document(collection = "books")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Book implements Copyable<Book> {
    @Id
    private long id;

    private String title;

    @DBRef
    private Author author;

    @DBRef
    private Genre genre;

    public Book(String title, Author author, Genre genre) {
        this.title = title;
        this.author = author;
        this.genre = genre;
    }

    @Override
    public Book copy() {
        return new Book(
                this.getId(),
                this.getTitle(),
                this.getAuthor().copy(),
                this.getGenre().copy()
        );
    }
}
