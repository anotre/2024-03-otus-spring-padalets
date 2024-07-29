package ru.otus.hw.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import ru.otus.hw.models.prototype.Copyable;

@Document(collection = "books")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Book implements Copyable<Book> {
    @Id
    private String id;

    private String title;

    private Author author;

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
