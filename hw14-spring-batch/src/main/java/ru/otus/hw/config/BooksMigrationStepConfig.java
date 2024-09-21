package ru.otus.hw.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.MongoPagingItemReader;
import org.springframework.batch.item.data.builder.MongoPagingItemReaderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.transaction.PlatformTransactionManager;
import ru.otus.hw.batch.CustomJdbcBatchItemWriter;
import ru.otus.hw.batch.CustomJdbcBatchItemWriterBuilder;
import ru.otus.hw.batch.jdbc.BookIdsPreparedStatementSetter;
import ru.otus.hw.batch.jdbc.BookPreparedStatementSetter;
import ru.otus.hw.models.mongodb.Book;

import javax.sql.DataSource;
import java.util.HashMap;

@Configuration
public class BooksMigrationStepConfig {
    public static final String MIGRATE_BOOKS_STEP_NAME = "migrateBooks";

    private final Logger logger = LoggerFactory.getLogger("Batch");

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private PlatformTransactionManager platformTransactionManager;

    @Bean
    public Step booksMigrationStep(ItemReader<Book> reader, ItemWriter<Book> writer) {

        return new StepBuilder(MIGRATE_BOOKS_STEP_NAME, jobRepository)
                .<Book, Book>chunk(JobConfig.CHUNK_SIZE, platformTransactionManager)
                .reader(reader)
                .writer(writer)
                .build();
    }

    @Bean
    public MongoPagingItemReader<Book> booksReader(MongoOperations mongoOperations) {
        return new MongoPagingItemReaderBuilder<Book>()
                .name("bookReader")
                .collection("books")
                .template(mongoOperations)
                .targetType(Book.class)
                .pageSize(100)
                .jsonQuery("{}")
                .sorts(new HashMap<>())
                .build();
    }

    @Bean
    public CustomJdbcBatchItemWriter<Book> booksWriter(
            DataSource dataSource,
            BookPreparedStatementSetter preparedStatementSetter,
            BookIdsPreparedStatementSetter idsPreparedStatementSetter
    ) {
        return new CustomJdbcBatchItemWriterBuilder<Book>(
                MIGRATE_BOOKS_STEP_NAME + "_write",
                JobConfig.CHUNK_SIZE)
                .dataSource(dataSource)
                .beanMapped()
                .sql("INSERT INTO books (title, author_id, genre_id) SELECT :title, " +
                        "(SELECT rdb_id from authors_ids where nosql_id = :id), " +
                        "(SELECT rdb_id from genres_ids where nosql_id = :id)")
                .entityPreparedStatementSetter(preparedStatementSetter)
                .afterWriteSql("INSERT INTO books_ids (rdb_id, nosql_id) VALUES (?, ?)")
                .idsMatchingPreparedStatementSetter(idsPreparedStatementSetter)
                .build();
    }
}