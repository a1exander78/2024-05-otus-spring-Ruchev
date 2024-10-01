package ru.otus.hw.shell;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

@RequiredArgsConstructor
@ShellComponent
public class BatchCommands {
    private final Job libraryMigrationJob;

    private final JobLauncher jobLauncher;

    @SuppressWarnings("unused")
    @ShellMethod(value = "startMigrationJob", key = "sm")
    public void startMigrationJob() throws Exception {
        JobExecution execution = jobLauncher.run(libraryMigrationJob, new JobParameters());
        System.out.println(execution);
    }
}
