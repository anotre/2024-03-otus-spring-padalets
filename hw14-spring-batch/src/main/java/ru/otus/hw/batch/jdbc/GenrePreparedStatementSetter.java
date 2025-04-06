package ru.otus.hw.batch.jdbc;

import org.springframework.stereotype.Component;
import ru.otus.hw.models.mongodb.Genre;

import java.sql.PreparedStatement;
import java.sql.SQLException;

@Component
public class GenrePreparedStatementSetter implements EntityPreparedStatementSetter {
    @Override
    public void setValues(PreparedStatement ps, Object object, Object... args) throws SQLException {
        ps.setString(1, ((Genre) object).getName());
    }
}
