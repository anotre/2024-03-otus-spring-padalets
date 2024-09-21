package ru.otus.hw.batch.jdbc;

import org.springframework.stereotype.Component;
import ru.otus.hw.models.mongodb.Author;

import java.sql.PreparedStatement;
import java.sql.SQLException;

@Component
public class AuthorPreparedStatementSetter implements EntityPreparedStatementSetter {

    @Override
    public void setValues(PreparedStatement ps, Object object, Object... args) throws SQLException {
        ps.setString(1, ((Author) object).getFullName());
    }
}
