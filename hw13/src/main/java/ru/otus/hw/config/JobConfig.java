package ru.otus.hw.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.job.flow.support.SimpleFlow;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;

@Configuration
public class JobConfig {
    public static final int CHUNK_SIZE = 3;

    public static final String LIBRARY_MIGRATION_JOB = "libraryMigrationJob";

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private AuthorJobConfig authorJobConfig;

    @Autowired
    private GenreJobConfig genreJobConfig;

    @Bean
    public Flow splitAuthorAndGenreMigration(Step transformAuthorStep, Step transformGenreStep) {
        var authorFlow = authorJobConfig.authorMigration(transformAuthorStep);
        var genreFlow = genreJobConfig.genreMigration(transformGenreStep);
        return new FlowBuilder<SimpleFlow>("splitAuthorAndGenreMigration")
                .split(new SimpleAsyncTaskExecutor())
                .add(authorFlow, genreFlow)
                .build();
    }

    @Bean
    public Job libraryMigrationJob(Step transformAuthorStep,
                                   Step transformGenreStep,
                                   Step transformBookStep,
                                   Step transformCommentStep) {
        return new JobBuilder(LIBRARY_MIGRATION_JOB, jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(splitAuthorAndGenreMigration(transformAuthorStep, transformGenreStep))
                .next(transformBookStep)
                .next(transformCommentStep)
                .build()
                .build();
    }
}
