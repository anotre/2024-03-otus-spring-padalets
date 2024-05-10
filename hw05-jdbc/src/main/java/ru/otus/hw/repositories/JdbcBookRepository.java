package ru.otus.hw.repositories;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JdbcBookRepository implements BookRepository {
    private final NamedParameterJdbcOperations jdbc;

    @Override
    public Optional<Book> findById(long id) {
        var params = Collections.singletonMap("id", id);
        try {
            var book = jdbc.queryForObject("select b.id, b.title, b.author_id, a.full_name, b.genre_id, g.name " +
                            "from books b " +
                            "join genres g on b.genre_id = g.id " +
                            "join authors a on b.author_id = a.id " +
                            "where b.id = :id",
                    params, new BookRowMapper());

            return Optional.ofNullable(book);
        } catch (EmptyResultDataAccessException exception) {
            return Optional.empty();
        }
    }

    @Override
    public List<Book> findAll() {
        return jdbc.getJdbcOperations().query(
                "select b.id, b.title, b.author_id, a.full_name, b.genre_id, g.name " +
                    "from books b " +
                    "join genres g on b.genre_id = g.id " +
                    "join authors a on b.author_id = a.id ",
                new BookRowMapper());
    }

    @Override
    public Book save(Book book) {
        if (book.getId() == 0) {
            return insert(book);
        }

        return update(book);
    }

    @Override
    public void deleteById(long id) {
        jdbc.update("delete from books where id = :id", Collections.singletonMap("id", id));
    }

    private Book insert(Book book) {
        var params = new MapSqlParameterSource();
        params.addValue("title", book.getTitle());
        params.addValue("author_id", book.getAuthor().getId());
        params.addValue("genre_id", book.getGenre().getId());
        KeyHolder kh = new GeneratedKeyHolder();
        jdbc.update("insert into books (title, genre_id, author_id) values (:title, :genre_id, :author_id)",
                params,
                kh,
                new String[] { "id" });

        //noinspection DataFlowIssue
        book.setId(kh.getKeyAs(Long.class));

        return book;
    }

    private Book update(Book book) {
        var params = new MapSqlParameterSource();
        params.addValue("id", book.getId());
        params.addValue("title", book.getTitle());
        params.addValue("author_id", book.getAuthor().getId());
        params.addValue("genre_id", book.getGenre().getId());
        var affectedRows = jdbc.update(
                "update books set title = :title, genre_id = :genre_id, author_id = :author_id where id = :id",
                params);

        if (affectedRows == 0) {
            throw new EntityNotFoundException("Update statement ");
        }

        return book;
    }

    private static class BookRowMapper implements RowMapper<Book> {

        @Override
        public Book mapRow(ResultSet rs, int rowNum) throws SQLException {
            var author = new Author(rs.getLong("author_id"), rs.getString("full_name"));
            var genre = new Genre(rs.getLong("genre_id"), rs.getString("name"));

            return new Book(
                    rs.getLong("id"),
                    rs.getString("title"),
                    author,
                    genre);
        }
    }
}
