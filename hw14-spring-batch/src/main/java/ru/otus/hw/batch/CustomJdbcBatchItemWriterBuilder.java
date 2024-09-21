package ru.otus.hw.batch;

import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.ItemPreparedStatementSetter;
import org.springframework.batch.item.database.ItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.database.support.ColumnMapItemPreparedStatementSetter;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.util.Assert;
import ru.otus.hw.batch.jdbc.EntityPreparedStatementSetter;

import javax.sql.DataSource;
import java.math.BigInteger;
import java.util.Map;

public class CustomJdbcBatchItemWriterBuilder<T> extends JdbcBatchItemWriterBuilder<T> {

    private boolean assertUpdates = true;

    private String sql;

    private ItemPreparedStatementSetter<T> itemPreparedStatementSetter;

    private ItemSqlParameterSourceProvider<T> itemSqlParameterSourceProvider;

    private DataSource dataSource;

    private NamedParameterJdbcOperations namedParameterJdbcTemplate;

    private BigInteger mapped = new BigInteger("0");

    private EntityPreparedStatementSetter preparedStatementSetter;

    private EntityPreparedStatementSetter idsMatchingPreparedStatementSetter;

    private final String stageName;

    private final int chunkSize;

    /**
     * Will be used by the {@link CustomJdbcBatchItemWriter} for additional query to datasource right after main
     * batch queries, and it'll have access to the new identifiers retrieved from them.
     * Supports sql statements with placeholders for prepared statement only.
     *
     * @param afterWriteSql sql statement.
     */
    private String afterWriteSql;

    public CustomJdbcBatchItemWriterBuilder(String stageName, int chunkSize) {
        this.stageName = stageName;
        this.chunkSize = chunkSize;
    }

    public CustomJdbcBatchItemWriterBuilder<T> entityPreparedStatementSetter(
            EntityPreparedStatementSetter preparedStatementSetter) {
        this.preparedStatementSetter = preparedStatementSetter;
        return this;
    }

    public CustomJdbcBatchItemWriterBuilder<T> idsMatchingPreparedStatementSetter(
            EntityPreparedStatementSetter idsMatchingPreparedStatementSetter) {
        this.idsMatchingPreparedStatementSetter = idsMatchingPreparedStatementSetter;
        return this;
    }

    public CustomJdbcBatchItemWriterBuilder<T> afterWriteSql(String afterWriteSql) {
        this.afterWriteSql = afterWriteSql;
        return this;
    }

    @Override
    public CustomJdbcBatchItemWriterBuilder<T> dataSource(DataSource dataSource) {
        this.dataSource = dataSource;
        return this;
    }

    @Override
    public CustomJdbcBatchItemWriterBuilder<T> assertUpdates(boolean assertUpdates) {
        this.assertUpdates = assertUpdates;
        return this;
    }

    @Override
    public CustomJdbcBatchItemWriterBuilder<T> sql(String sql) {
        this.sql = sql;
        return this;
    }

    @Override
    public CustomJdbcBatchItemWriterBuilder<T> itemPreparedStatementSetter(
            ItemPreparedStatementSetter<T> itemPreparedStatementSetter) {
        this.itemPreparedStatementSetter = itemPreparedStatementSetter;
        return this;
    }

    @Override
    public CustomJdbcBatchItemWriterBuilder<T> itemSqlParameterSourceProvider(
            ItemSqlParameterSourceProvider<T> itemSqlParameterSourceProvider) {
        this.itemSqlParameterSourceProvider = itemSqlParameterSourceProvider;
        return this;
    }

    @Override
    public CustomJdbcBatchItemWriterBuilder<T> namedParametersJdbcTemplate(
            NamedParameterJdbcOperations namedParameterJdbcOperations) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        return this;
    }

    @Override
    public CustomJdbcBatchItemWriterBuilder<T> columnMapped() {
        this.mapped = this.mapped.setBit(0);

        return this;
    }

    @Override
    public CustomJdbcBatchItemWriterBuilder<T> beanMapped() {
        this.mapped = this.mapped.setBit(1);

        return this;
    }

    @Override
    public CustomJdbcBatchItemWriter<T> build() {
        this.checkBuildArgs();
        var writer = new CustomJdbcBatchItemWriter<T>(this.stageName, this.chunkSize);
        writer.setSql(this.sql);
        writer.setAssertUpdates(this.assertUpdates);
        writer.setItemSqlParameterSourceProvider(this.itemSqlParameterSourceProvider);
        writer.setItemPreparedStatementSetter(this.itemPreparedStatementSetter);
        int mappedValue = this.mapped.intValue();
        if (mappedValue == 1) {
            ((JdbcBatchItemWriter<Map<String, Object>>) writer)
                    .setItemPreparedStatementSetter(new ColumnMapItemPreparedStatementSetter());
        } else if (mappedValue == 2) {
            writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>());
        }
        if (this.dataSource != null) {
            this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(this.dataSource);
        }
        writer.setJdbcTemplate(this.namedParameterJdbcTemplate);
        writer.setEntityPreparedStatementSetter(this.preparedStatementSetter);
        if (this.afterWriteSql != null) {
            writer.setAfterWriteSql(this.afterWriteSql);
            writer.setIdsMatchingPreparedStatementSetter(this.idsMatchingPreparedStatementSetter);
        }
        return writer;
    }

    private void checkBuildArgs() {
        Assert.state(this.dataSource != null || this.namedParameterJdbcTemplate != null,
                "Either a DataSource or a NamedParameterJdbcTemplate is required");

        Assert.notNull(this.sql, "A SQL statement is required");
        Assert.state(
                this.mapped.intValue() != 3,
                "Either an item can be mapped via db column or via bean spec, can't be both");
        Assert.notNull(this.preparedStatementSetter, "PreparedStatementSetter field is required");
        if (this.afterWriteSql != null) {
            Assert.notNull(
                    this.idsMatchingPreparedStatementSetter,
                    "IdsMatchingPreparedStatementSetter field is required");
        }
    }
}
