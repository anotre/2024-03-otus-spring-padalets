package ru.otus.hw.batch;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStream;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import ru.otus.hw.batch.jdbc.EntityPreparedStatementSetter;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;

@RequiredArgsConstructor
@Setter
public class CustomJdbcBatchItemWriter<T> extends JdbcBatchItemWriter<T> implements ItemStream {

    private String afterWriteSql;

    private EntityPreparedStatementSetter entityPreparedStatementSetter;

    private EntityPreparedStatementSetter idsMatchingPreparedStatementSetter;

    private final String stageName;

    private final int chunkSize;

    @Override
    public void afterPropertiesSet() {
        super.afterPropertiesSet();
    }

    @SuppressWarnings("unchecked")
    @Override
    public void write(final Chunk<? extends T> chunk) throws Exception {
        if (!chunk.isEmpty()) {
            if (logger.isDebugEnabled()) {
                logger.debug("Executing batch with " + chunk.size() + " items.");
            }

            int[] updateCounts;
            KeyHolder kh = new GeneratedKeyHolder();
            if (usingNamedParameters) {
                updateCounts = this.namedParamsBatchUpdate(chunk, kh);
            } else {
                updateCounts = preparedStatementBatchUpdate(chunk, kh);
            }

            if (assertUpdates) {
                this.checkAffectedUpdates(chunk, updateCounts);
            }

            if (this.afterWriteSql != null) {
                nextWrite(chunk, kh);
            }

            processUpdateCounts(updateCounts);
        }
    }

    private void nextWrite(Chunk<? extends T> chunk, KeyHolder kh) {
        var chunkItems = chunk.getItems();
        var keys = kh.getKeyList();
        namedParameterJdbcTemplate.getJdbcOperations().batchUpdate(afterWriteSql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                var key = keys.get(i).get("id");
                var chunkItem = chunkItems.get(i);
                idsMatchingPreparedStatementSetter.setValues(ps, chunkItem, key);
            }

            @Override
            public int getBatchSize() {
                return chunk.size();
            }
        });
    }

    private int[] namedParamsBatchUpdate(final Chunk<? extends T> chunk, KeyHolder kh) {
        if (chunk.getItems().get(0) instanceof Map && this.itemSqlParameterSourceProvider == null) {
            var namedParams = chunk.getItems().stream().map(chunkItem ->
                    new MapSqlParameterSource((Map<String, ?>) chunkItem)).toArray(SqlParameterSource[]::new);
            return namedParameterJdbcTemplate.batchUpdate(sql, namedParams, kh);
        } else {
            SqlParameterSource[] batchArgs = new SqlParameterSource[chunk.size()];
            int i = 0;
            for (T item : chunk) {
                batchArgs[i++] = itemSqlParameterSourceProvider.createSqlParameterSource(item);
            }
            return namedParameterJdbcTemplate.batchUpdate(sql, batchArgs, kh);
        }
    }

    private int[] preparedStatementBatchUpdate(final Chunk<? extends T> chunk, KeyHolder kh) {
        var items = chunk.getItems();
        var preparedStatementCreator = (PreparedStatementCreator) con ->
                con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);


        int[] updates = namedParameterJdbcTemplate.getJdbcOperations()
                .batchUpdate(preparedStatementCreator, new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        entityPreparedStatementSetter.setValues(ps, items.get(i));
                    }

                    @Override
                    public int getBatchSize() {
                        return chunk.size();
                    }
                }, kh);

        return updates;
    }

    private void checkAffectedUpdates(final Chunk<? extends T> chunk, int[] updateCounts) {
        for (int i = 0; i < updateCounts.length; i++) {
            int value = updateCounts[i];
            if (value == 0) {
                throw new EmptyResultDataAccessException("Item " + i + " of " + updateCounts.length
                        + " did not update any rows: [" + chunk.getItems().get(i) + "]", 1);
            }
        }
    }

    @Override
    public void open(ExecutionContext executionContext) throws ItemStreamException {
        executionContext.getLong(stageName, 0);
        ItemStream.super.open(executionContext);
    }

    @Override
    public void update(ExecutionContext executionContext) throws ItemStreamException {
        var writtenRecords = executionContext.getLong(stageName, 0);

        executionContext.putLong(stageName, writtenRecords + this.chunkSize);
        ItemStream.super.update(executionContext);
    }
}
