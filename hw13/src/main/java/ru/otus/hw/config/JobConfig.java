package ru.otus.hw.config;

import jakarta.persistence.EntityManagerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
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
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.transaction.PlatformTransactionManager;
import ru.otus.hw.model.h2.Author;
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
    public ItemProcessor<Author, ru.otus.hw.model.mongo.Author> authorProcessor(MigrationService migrationService) {
        return migrationService::migrateAuthor;
    }

    @Bean
    public MongoItemWriter<ru.otus.hw.model.mongo.Author> authorWriter() {
        return new MongoItemWriterBuilder<ru.otus.hw.model.mongo.Author>()
                .template(mongoTemplate)
                .collection("Author")
                .build();
    }

    @Bean
    public Step transformAuthorStep(JpaPagingItemReader<Author> authorReader,
                                    MongoItemWriter<ru.otus.hw.model.mongo.Author> authorWriter,
                                    ItemProcessor<Author, ru.otus.hw.model.mongo.Author> authorProcessor) {
        return new StepBuilder("transformAuthorStep", jobRepository)
                .<Author, ru.otus.hw.model.mongo.Author>chunk(CHUNK_SIZE, platformTransactionManager)
                .reader(authorReader)
                .processor(authorProcessor)
                .writer(authorWriter)
//                .taskExecutor(new SimpleAsyncTaskExecutor())
                .build();
    }

    @Bean
    public Job authorMigrationJob(Step transformAuthorStep) {
        return new JobBuilder(AUTHOR_MIGRATION_JOB, jobRepository)
                .incrementer(new RunIdIncrementer())
                .flow(transformAuthorStep)
                .end()
                .build();
    }
}
