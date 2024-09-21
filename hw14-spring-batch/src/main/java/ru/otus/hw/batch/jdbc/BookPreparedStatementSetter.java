package ru.otus.hw.batch.jdbc;

import org.springframework.stereotype.Component;
import ru.otus.hw.models.mongodb.Book;

import java.sql.PreparedStatement;
import java.sql.SQLException;

@Component
public class BookPreparedStatementSetter implements EntityPreparedStatementSetter {
    @Override
    public void setValues(PreparedStatement ps, Object object, Object... args) throws SQLException {
        Book book = (Book) object;
        ps.setString(1, book.getTitle());
        ps.setString(2, book.getAuthor().getId());
        ps.setString(3, book.getGenre().getId());
    }
}
