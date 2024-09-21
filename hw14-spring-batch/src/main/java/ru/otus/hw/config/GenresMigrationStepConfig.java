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
import ru.otus.hw.batch.jdbc.GenreIdsPreparedStatementSetter;
import ru.otus.hw.batch.jdbc.GenrePreparedStatementSetter;
import ru.otus.hw.models.mongodb.Genre;

import javax.sql.DataSource;
import java.util.HashMap;

@Configuration
public class GenresMigrationStepConfig {
    public static final String MIGRATE_GENRES_STEP_NAME = "migrateGenres";

    private final Logger logger = LoggerFactory.getLogger("Batch");

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private PlatformTransactionManager platformTransactionManager;

    @Bean
    public Step genresMigrationStep(ItemReader<Genre> reader, ItemWriter<Genre> writer) {

        return new StepBuilder(MIGRATE_GENRES_STEP_NAME, jobRepository)
                .<Genre, Genre>chunk(JobConfig.CHUNK_SIZE, platformTransactionManager)
                .reader(reader)
                .writer(writer)
                .build();
    }

    @Bean
    public MongoPagingItemReader<Genre> genresReader(MongoOperations mongoOperations) {
        return new MongoPagingItemReaderBuilder<Genre>()
                .name("genreReader")
                .collection("genres")
                .template(mongoOperations)
                .targetType(Genre.class)
                .pageSize(100)
                .jsonQuery("{}")
                .sorts(new HashMap<>())
                .build();
    }

    @Bean
    public CustomJdbcBatchItemWriter<Genre> genresWriter(
            DataSource dataSource,
            GenrePreparedStatementSetter preparedStatementSetter,
            GenreIdsPreparedStatementSetter idsPreparedStatementSetter
    ) {
        return new CustomJdbcBatchItemWriterBuilder<Genre>(
                MIGRATE_GENRES_STEP_NAME + "_write",
                JobConfig.CHUNK_SIZE)
                .dataSource(dataSource)
                .beanMapped()
                .sql("INSERT INTO genres (name) VALUES (:name)")
                .entityPreparedStatementSetter(preparedStatementSetter)
                .afterWriteSql("INSERT INTO genres_ids (rdb_id, nosql_id) VALUES (?, ?)")
                .idsMatchingPreparedStatementSetter(idsPreparedStatementSetter)
                .build();
    }
}