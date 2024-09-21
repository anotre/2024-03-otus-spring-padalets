package ru.otus.hw.batch.jdbc;

import org.springframework.stereotype.Component;
import ru.otus.hw.exceptions.GeneratedKeyArgumentNotFoundException;
import ru.otus.hw.models.mongodb.Genre;

import java.sql.PreparedStatement;
import java.sql.SQLException;

@Component
public class GenreIdsPreparedStatementSetter implements EntityPreparedStatementSetter {
    @Override
    public void setValues(PreparedStatement ps, Object object, Object... args) throws SQLException {
        if (args[0] == null) {
            throw new GeneratedKeyArgumentNotFoundException(
                    "Generated key for entity with source id " +
                            ((Genre) object).getId() +
                            " was not found");
        }

        ps.setLong(1, (long) args[0]);
        ps.setString(2, ((Genre) object).getId());
    }
}
