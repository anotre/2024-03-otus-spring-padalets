package ru.otus.hw.batch.jdbc;

import org.springframework.stereotype.Component;
import ru.otus.hw.models.mongodb.Comment;

import java.sql.PreparedStatement;
import java.sql.SQLException;

@Component
public class CommentPreparedStatementSetter implements EntityPreparedStatementSetter {
    @Override
    public void setValues(PreparedStatement ps, Object object, Object... args) throws SQLException {
        Comment comment = (Comment) object;
        ps.setString(1, comment.getText());
        ps.setString(2, comment.getBook().getId());
    }
}
