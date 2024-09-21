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
import ru.otus.hw.batch.jdbc.CommentPreparedStatementSetter;
import ru.otus.hw.models.mongodb.Comment;

import javax.sql.DataSource;
import java.util.HashMap;

@Configuration
public class CommentsMigrationStepConfig {
    public static final String MIGRATE_COMMENTS_STEP_NAME = "migrateComments";

    private final Logger logger = LoggerFactory.getLogger("Batch");

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private PlatformTransactionManager platformTransactionManager;

    @Bean
    public Step commentsMigrationStep(ItemReader<Comment> reader, ItemWriter<Comment> writer) {

        return new StepBuilder(MIGRATE_COMMENTS_STEP_NAME, jobRepository)
                .<Comment, Comment>chunk(JobConfig.CHUNK_SIZE, platformTransactionManager)
                .reader(reader)
                .writer(writer)
                .build();
    }

    @Bean
    public MongoPagingItemReader<Comment> commentsReader(MongoOperations mongoOperations) {
        return new MongoPagingItemReaderBuilder<Comment>()
                .name("commentReader")
                .collection("comments")
                .template(mongoOperations)
                .targetType(Comment.class)
                .pageSize(100)
                .jsonQuery("{}")
                .sorts(new HashMap<>())
                .build();
    }

    @Bean
    public CustomJdbcBatchItemWriter<Comment> commentsWriter(
            DataSource dataSource,
            CommentPreparedStatementSetter preparedStatementSetter
    ) {
        return new CustomJdbcBatchItemWriterBuilder<Comment>(
                MIGRATE_COMMENTS_STEP_NAME + "_write",
                JobConfig.CHUNK_SIZE)
                .dataSource(dataSource)
                .beanMapped()
                .sql("INSERT INTO comments (text, book_id) SELECT :text, " +
                        "(SELECT rdb_id from books_ids where nosql_id = :id)")
                .entityPreparedStatementSetter(preparedStatementSetter)
                .build();
    }
}