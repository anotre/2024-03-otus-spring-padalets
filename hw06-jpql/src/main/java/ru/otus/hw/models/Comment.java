package ru.otus.hw.models;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedAttributeNode;
import jakarta.persistence.NamedEntityGraph;
import jakarta.persistence.NamedSubgraph;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import ru.otus.hw.models.prototype.Copyable;

@Entity
@Table(name = "comments")
@NamedEntityGraph(name = "comment-entity-graph",
        attributeNodes = {@NamedAttributeNode(value = "book", subgraph = "book-entity-subgraph")},
        subgraphs = {
                @NamedSubgraph(
                        name = "book-entity-subgraph",
                        attributeNodes = {
                                @NamedAttributeNode("author"),
                                @NamedAttributeNode("genre"),
                        }
                )
        }
)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Comment implements Copyable<Comment> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "text", nullable = false, unique = true)
    private String text;

    @ManyToOne(targetEntity = Book.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Book book;

    @Override
    public Comment copy() {
        return new Comment(
                this.getId(),
                this.getText(),
                this.getBook().copy()
        );
    }
}
