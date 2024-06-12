package ru.otus.hw.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import ru.otus.hw.models.prototype.Copyable;

@Document(collection = "comments")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Comment implements Copyable<Comment> {
    @Id
    private long id;

    private String text;

    @DBRef
    private Book book;

    public Comment(String text, Book book) {
        this.text = text;
        this.book = book;
    }

    @Override
    public Comment copy() {
        return new Comment(
                this.getId(),
                this.getText(),
                this.getBook().copy()
        );
    }
}
