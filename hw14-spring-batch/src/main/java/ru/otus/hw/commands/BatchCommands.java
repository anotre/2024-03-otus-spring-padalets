package ru.otus.hw.commands;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.util.Assert;

import java.util.Properties;

import static ru.otus.hw.config.JobConfig.MIGRATE_LIBRARY_JOB_NAME;

@ShellComponent
@RequiredArgsConstructor
public class BatchCommands {

    private final JobOperator jobOperator;

    private final JobExplorer jobExplorer;

    @ShellMethod(value = "Start migration from mongodb to h2", key = "sm")
    public void startMigrationJob() throws Exception {
        jobOperator.start(MIGRATE_LIBRARY_JOB_NAME, new Properties());
    }

    @ShellMethod(value = "Restart last failed migration from mongodb to h2", key = "rlfm")
    public void restartMigrationJob() throws Exception {
        var jobInstance = jobExplorer.getLastJobInstance(MIGRATE_LIBRARY_JOB_NAME);
        Assert.notNull(jobInstance, "No such job found.");
        var lastExecution = jobExplorer.getLastJobExecution(jobInstance);
        Assert.notNull(lastExecution, "Job never been executed.");
        jobOperator.restart(lastExecution.getId());
    }
}
