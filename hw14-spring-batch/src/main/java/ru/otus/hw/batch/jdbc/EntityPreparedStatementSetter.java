package ru.otus.hw.batch.jdbc;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface EntityPreparedStatementSetter {
    void setValues(PreparedStatement ps, Object object, Object... args) throws SQLException;
}
