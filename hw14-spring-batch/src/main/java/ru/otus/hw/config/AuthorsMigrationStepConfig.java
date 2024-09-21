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
import ru.otus.hw.batch.jdbc.AuthorIdsPreparedStatementSetter;
import ru.otus.hw.batch.jdbc.AuthorPreparedStatementSetter;
import ru.otus.hw.models.mongodb.Author;

import javax.sql.DataSource;
import java.util.HashMap;

@Configuration
public class AuthorsMigrationStepConfig {
    public static final String MIGRATE_AUTHORS_STEP_NAME = "migrateAuthors";

    private final Logger logger = LoggerFactory.getLogger("Batch");

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private PlatformTransactionManager platformTransactionManager;

    @Bean
    public Step authorsMigrationStep(ItemReader<Author> reader, ItemWriter<Author> writer) {

        return new StepBuilder(MIGRATE_AUTHORS_STEP_NAME, jobRepository)
                .<Author, Author>chunk(JobConfig.CHUNK_SIZE, platformTransactionManager)
                .reader(reader)
                .writer(writer)
                .build();
    }

    @Bean
    public MongoPagingItemReader<Author> authorsReader(MongoOperations mongoOperations) {
        return new MongoPagingItemReaderBuilder<Author>()
                .name("authorReader")
                .collection("authors")
                .template(mongoOperations)
                .targetType(Author.class)
                .pageSize(100)
                .jsonQuery("{}")
                .sorts(new HashMap<>())
                .build();
    }

    @Bean
    public CustomJdbcBatchItemWriter<Author> authorsWriter(
            DataSource dataSource,
            AuthorPreparedStatementSetter preparedStatementSetter,
            AuthorIdsPreparedStatementSetter idsPreparedStatementSetter
    ) {
        return new CustomJdbcBatchItemWriterBuilder<Author>(
                MIGRATE_AUTHORS_STEP_NAME + "_write",
                JobConfig.CHUNK_SIZE)
                .dataSource(dataSource)
                .beanMapped()
                .sql("INSERT INTO authors (full_name) VALUES (:fullName)")
                .entityPreparedStatementSetter(preparedStatementSetter)
                .afterWriteSql("INSERT INTO authors_ids (rdb_id, nosql_id) VALUES (?, ?)")
                .idsMatchingPreparedStatementSetter(idsPreparedStatementSetter)
                .build();
    }
}