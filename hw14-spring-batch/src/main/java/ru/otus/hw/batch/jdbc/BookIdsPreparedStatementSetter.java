package ru.otus.hw.batch.jdbc;

import org.springframework.stereotype.Component;
import ru.otus.hw.exceptions.GeneratedKeyArgumentNotFoundException;
import ru.otus.hw.models.mongodb.Book;

import java.sql.PreparedStatement;
import java.sql.SQLException;

@Component
public class BookIdsPreparedStatementSetter implements EntityPreparedStatementSetter {
    @Override
    public void setValues(PreparedStatement ps, Object object, Object... args) throws SQLException {
        if (args[0] == null) {
            throw new GeneratedKeyArgumentNotFoundException(
                    "Generated key for entity with source id " +
                            ((Book) object).getId() +
                            " was not found");
        }

        ps.setLong(1, (long) args[0]);
        ps.setString(2, ((Book) object).getId());
    }
}
