package ru.otus.hw.config;

import jakarta.persistence.EntityManagerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.job.flow.support.SimpleFlow;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.data.MongoItemWriter;
import org.springframework.batch.item.data.builder.MongoItemWriterBuilder;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.transaction.PlatformTransactionManager;
import ru.otus.hw.model.h2.Author;
import ru.otus.hw.model.h2.Genre;
import ru.otus.hw.service.MigrationService;

@Configuration
public class JobConfig {
    public static final int CHUNK_SIZE = 3;

    public static final String AUTHOR_MIGRATION_JOB = "authorMigrationJob";

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private PlatformTransactionManager platformTransactionManager;

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Bean
    public JpaPagingItemReader<Author> authorReader() {
        return new JpaPagingItemReaderBuilder<Author>()
                .name("authorReader")
                .entityManagerFactory(entityManagerFactory)
                .queryString("select a from Author a")
                .pageSize(3)
                .build();
    }

    @Bean
    public JpaPagingItemReader<Genre> genreReader() {
        return new JpaPagingItemReaderBuilder<Genre>()
                .name("genreReader")
                .entityManagerFactory(entityManagerFactory)
                .queryString("select g from Genre g")
                .pageSize(3)
                .build();
    }

    @Bean
    public ItemProcessor<Author, ru.otus.hw.model.mongo.Author> authorProcessor(MigrationService migrationService) {
        return migrationService::migrateAuthor;
    }

    @Bean
    public ItemProcessor<Genre, ru.otus.hw.model.mongo.Genre> genreProcessor(MigrationService migrationService) {
        return migrationService::migrateGenre;
    }

    @Bean
    public MongoItemWriter<ru.otus.hw.model.mongo.Author> authorWriter() {
        return new MongoItemWriterBuilder<ru.otus.hw.model.mongo.Author>()
                .template(mongoTemplate)
                .collection("Author")
                .build();
    }

    @Bean
    public MongoItemWriter<ru.otus.hw.model.mongo.Genre> genreWriter() {
        return new MongoItemWriterBuilder<ru.otus.hw.model.mongo.Genre>()
                .template(mongoTemplate)
                .collection("Genre")
                .build();
    }

    @Bean
    public Step transformAuthorStep(JpaPagingItemReader<Author> authorReader,
                                    ItemProcessor<Author, ru.otus.hw.model.mongo.Author> authorProcessor,
                                    MongoItemWriter<ru.otus.hw.model.mongo.Author> authorWriter) {
        return new StepBuilder("transformAuthorStep", jobRepository)
                .<Author, ru.otus.hw.model.mongo.Author>chunk(CHUNK_SIZE, platformTransactionManager)
                .reader(authorReader)
                .processor(authorProcessor)
                .writer(authorWriter)
//                .taskExecutor(new SimpleAsyncTaskExecutor())
                .build();
    }

    @Bean
    public Step transformGenreStep(JpaPagingItemReader<Genre> genreReader,
                                    ItemProcessor<Genre, ru.otus.hw.model.mongo.Genre> genreProcessor,
                                    MongoItemWriter<ru.otus.hw.model.mongo.Genre> genreWriter) {
        return new StepBuilder("transformGenreStep", jobRepository)
                .<Genre, ru.otus.hw.model.mongo.Genre>chunk(CHUNK_SIZE, platformTransactionManager)
                .reader(genreReader)
                .processor(genreProcessor)
                .writer(genreWriter)
//                .taskExecutor(new SimpleAsyncTaskExecutor())
                .build();
    }

    @Bean
    public Job authorAndGenreMigrationJob(Step transformAuthorStep, Step transformGenreStep) {
        return new JobBuilder(AUTHOR_MIGRATION_JOB, jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(splitAuthorAndGenreMigration(transformAuthorStep, transformGenreStep))
                .build()
                .build();
    }

    @Bean
    public Flow authorMigration(Step transformAuthorStep) {
        return new FlowBuilder<SimpleFlow>("authorMigration")
                .start(transformAuthorStep)
                .build();
    }

    @Bean
    public Flow genreMigration(Step transformGenreStep) {
        return new FlowBuilder<SimpleFlow>("genreMigration")
                .start(transformGenreStep)
                .build();
    }

    @Bean
    public Flow splitAuthorAndGenreMigration(Step transformAuthorStep, Step transformGenreStep) {
        return new FlowBuilder<SimpleFlow>("splitAuthorAndGenreMigration")
                .split(new SimpleAsyncTaskExecutor())
                .add(authorMigration(transformAuthorStep), genreMigration(transformGenreStep))
                .build();
    }
}
