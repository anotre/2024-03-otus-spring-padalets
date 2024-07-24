package ru.otus.hw.mongock.changelog.test;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;

import com.mongodb.client.MongoDatabase;
import reactor.core.publisher.Flux;
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

    private List<Genre> genres;

    private List<Author> authors;

    private List<Book> books;

    private List<Comment> comments;

    @ChangeSet(order = "001", id = "dropDb", runAlways = true, author = MAIN_AUTHOR)
    public void dropDb(MongoDatabase db) {
        db.drop();
    }

    @ChangeSet(order = "002", id = "insertGenres", author = MAIN_AUTHOR)
    public void insertGenres(GenreRepository repository) {
        this.genres = Flux.fromIterable(List.of(
                new Genre("testGenreId", "Genre_1"),
                new Genre("Genre_2"),
                new Genre("Genre_3")
        )).flatMap(repository::save).collectList().block();
    }

    @ChangeSet(order = "003", id = "insertAuthors", author = MAIN_AUTHOR)
    public void insertAuthors(AuthorRepository repository) {
        this.authors = Flux.fromIterable(List.of(
                new Author("testAuthorId", "Author_1"),
                new Author("Author_2"),
                new Author("Author_3")
        )).flatMap(repository::save).collectList().block();
    }

    @ChangeSet(order = "004", id = "insertBooks", author = MAIN_AUTHOR)
    public void insertBooks(BookRepository repository) {
        this.books = Flux.fromIterable(List.of(
                new Book("testBookId", "BookTitle_1", authors.get(0), genres.get(0)),
                new Book("BookTitle_2", authors.get(1), genres.get(1)),
                new Book("BookTitle_3", authors.get(2), genres.get(2))
        )).flatMap(repository::save).collectList().block();
    }

    @ChangeSet(order = "005", id = "insertComments", author = MAIN_AUTHOR)
    public void insertComments(CommentRepository repository) {
        this.comments = Flux.fromIterable(List.of(
                new Comment("testCommentId", "Comment_1", books.get(0)),
                new Comment("Comment_2", books.get(1)),
                new Comment("Comment_3", books.get(2))
        )).flatMap(repository::save).collectList().block();
    }
}
