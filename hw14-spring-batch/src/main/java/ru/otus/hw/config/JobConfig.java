package ru.otus.hw.config;

import jakarta.persistence.EntityManagerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.job.flow.support.SimpleFlow;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.MethodInvokingTaskletAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;
import ru.otus.hw.service.batch.TempTablesService;

@Configuration
public class JobConfig {

    public static final int CHUNK_SIZE = 2;

    public static final String MIGRATE_LIBRARY_JOB_NAME = "migrateLibrary";

    private final Logger logger = LoggerFactory.getLogger("Batch");

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private PlatformTransactionManager platformTransactionManager;

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    @Bean
    public Job migrateLibraryJob(
            SimpleFlow splitFlow,
            Step booksMigrationStep,
            Step commentsMigrationStep,
            Step cleanUpStep) {
        return new JobBuilder(MIGRATE_LIBRARY_JOB_NAME, jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(splitFlow)
                .next(booksMigrationStep)
                .next(commentsMigrationStep)
                .next(cleanUpStep)
                .end()
                .build();
    }

    @Bean
    public TaskExecutor taskExecutor() {
        return new SimpleAsyncTaskExecutor("spring_batch");
    }

    @Bean
    public SimpleFlow splitFlow(SimpleFlow authorsFlow, SimpleFlow genresFlow) {
        return new FlowBuilder<SimpleFlow>("splitFlow")
                .split(taskExecutor())
                .add(authorsFlow, genresFlow)
                .build();
    }

    @Bean
    public SimpleFlow authorsFlow(Step authorsMigrationStep) {
        return new FlowBuilder<SimpleFlow>("authorsFlow")
                .start(authorsMigrationStep)
                .build();
    }

    @Bean
    public SimpleFlow genresFlow(Step genresMigrationStep) {
        return new FlowBuilder<SimpleFlow>("authorsFlow")
                .start(genresMigrationStep)
                .build();
    }

    @Bean
    public Step cleanUpStep(MethodInvokingTaskletAdapter cleanUpTaskletAdapter) {
        return new StepBuilder("cleanUpStep", jobRepository)
                .tasklet(cleanUpTaskletAdapter, platformTransactionManager)
                .build();
    }

    @Bean
    public MethodInvokingTaskletAdapter cleanUpTaskletAdapter(TempTablesService tempTablesService) {
        var taskletAdapter = new MethodInvokingTaskletAdapter();
        taskletAdapter.setTargetObject(tempTablesService);
        taskletAdapter.setTargetMethod("dropTempTables");

        return taskletAdapter;
    }
}