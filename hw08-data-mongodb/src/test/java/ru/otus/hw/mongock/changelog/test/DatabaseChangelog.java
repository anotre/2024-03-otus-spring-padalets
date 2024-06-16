package ru.otus.hw.mongock.changelog.test;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.mongodb.client.MongoDatabase;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.models.Genre;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.CommentRepository;
import ru.otus.hw.repositories.GenreRepository;

import java.util.List;

@ChangeLog
public class DatabaseChangelog {
    private static final String MAIN_AUTHOR = "mongock";

    private final List<Genre> genres = List.of(
            new Genre("1","Genre_1"),
            new Genre("2","Genre_2"),
            new Genre("3","Genre_3")
    );

    private final List<Author> authors = List.of(
            new Author("1","Author_1"),
            new Author("2","Author_2"),
            new Author("3","Author_3")
    );

    private final List<Book> books = List.of(
            new Book("1","BookTitle_1", authors.get(0), genres.get(0)),
            new Book("2","BookTitle_2", authors.get(1), genres.get(1)),
            new Book("3","BookTitle_3", authors.get(2), genres.get(2))
    );

    private final List<Comment> comments = List.of(
            new Comment("1","Comment_1", books.get(0)),
            new Comment("2","Comment_2", books.get(1)),
            new Comment("3","Comment_3", books.get(2))

    );

    @ChangeSet(order = "001", id = "dropDb", runAlways = true, author = MAIN_AUTHOR)
    public void dropDb(MongoDatabase db) {
        db.drop();
    }

    @ChangeSet(order = "002", id = "insertGenres", author = MAIN_AUTHOR)
    public void insertGenres(GenreRepository repository) {
        genres.forEach(repository::save);
    }

    @ChangeSet(order = "003", id = "insertAuthors", author = MAIN_AUTHOR)
    public void insertAuthors(AuthorRepository repository) {
        authors.forEach(repository::save);
    }

    @ChangeSet(order = "004", id = "insertBooks", author = MAIN_AUTHOR)
    public void insertBooks(BookRepository repository) {
        books.forEach(repository::save);
    }

    @ChangeSet(order = "005", id = "insertComments", author = MAIN_AUTHOR)
    public void insertComments(CommentRepository repository) {
        comments.forEach(repository::save);
    }
}
